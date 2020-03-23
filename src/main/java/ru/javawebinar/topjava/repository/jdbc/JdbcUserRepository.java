package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Email;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;


    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;

    }

    @Override
    @Transactional
    public User save(User user) {
        ValidationUtil.validateAndThrowError(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            addRoles(user);
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        }

        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) throws IllegalArgumentException {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        return setRoles(DataAccessUtils.singleResult(users));
    }

    @Override
    public User getByEmail(@Email String email) throws IllegalArgumentException {
//        checkByEmailArgs(email);

//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return setRoles(DataAccessUtils.singleResult(users));
    }

    @Override
    public List<User> getAll() {
        final String getAllRolesSql = "SELECT * FROM user_roles";
        final String getAllUsersSql = "SELECT * FROM users " + "ORDER BY name, email";

        Map<Integer, Set<Role>> userIsAndRoles = jdbcTemplate.query(getAllRolesSql,
                rs -> {
                    Map<Integer, Set<Role>> result = new HashMap<>();
                    while (rs.next()) {
                        Integer userId = rs.getInt("user_id");
                        Role role = Role.valueOf(rs.getString("role"));
                        result.computeIfAbsent(userId, integer -> new HashSet<>()).add(role);
                    }
                    return result;
                });

        List<User> userList = jdbcTemplate.query(getAllUsersSql, ROW_MAPPER);
        userList.forEach(user -> user.setRoles(userIsAndRoles.get(user.getId())));
        return userList;
    }

    private void addRoles(User user) {
        ValidationUtil.validateAndThrowError(user);
        Set<Role> roles = user.getRoles();
        String sql = "INSERT INTO user_roles (user_id, role) VALUES (?, ?)";

        //13.4.2 https://docs.spring.io/spring/docs/4.0.x/spring-framework-reference/html/jdbc.html
//        List<Object[]> batch = new ArrayList<>();
//        roles.forEach(role ->  batch.add(new Object[] {user.getId(), role.name()}));
//        jdbcTemplate.batchUpdate(sql, batch);

        //See 13.4 https://docs.spring.io/spring/docs/4.0.x/spring-framework-reference/html/jdbc.html
        jdbcTemplate.batchUpdate(
                sql,
                roles,
                roles.size(),
                new ParameterizedPreparedStatementSetter<Role>() {
                    @Override
                    public void setValues(PreparedStatement ps, Role role) throws SQLException {
                        ps.setInt(1, user.getId());
                        ps.setString(2, role.name());
                    }
                });
    }

    private User setRoles(User user) {
        ValidationUtil.validateAndThrowError(user);
//        if (user == null) return user; //validator throws Ex
        String setRolesSql = "SELECT * FROM user_roles WHERE user_id = ?";
        Object[] objects = {user.getId()};

        List<Role> roles = jdbcTemplate.query(setRolesSql, objects,
                (rs, rowNum) -> Role.valueOf(rs.getString("role")));
        user.setRoles(roles);
        return user;
    }

    @Override
    public void deleteUserRoles(User user) {
        ValidationUtil.validateAndThrowError(user);
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
    }

//    public void checkByEmailArgs(String email) {
//        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
//        ExecutableValidator executableValidator = validatorFactory.getValidator().forExecutables();
//
//        Method method = null;
//        try {
//            method = this.getClass().getMethod("getByEmail", String.class);
//            executableValidator.validateParameters(this, method, new Object[]{email});
//
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
//
////        Set<ConstraintViolation<EventHandler>> argsViolations = executableValidator
////                .validateParameters(eventHandler, method, new Object[]{event});
//    }

}

/*
// constructor args validation
Constructor<EventHandler> constructor = EventHandler.class.getConstructor(String.class);
Set<ConstraintViolation<EventHandler>> constructorViolations = executableValidator
        .validateConstructorParameters(constructor, new Object[]{"name"});

EventHandler eventHandler = new EventHandler(repository);
Event event = new Event();

// method args validation
Method method = eventHandler.getClass().getMethod("handle", Event.class);

Set<ConstraintViolation<EventHandler>> argsViolations = executableValidator
        .validateParameters(eventHandler, method, new Object[]{event});

// returned value validation
String id = eventHandler.handle(event);

Set<ConstraintViolation<EventHandler>> returnViolations = executableValidator
        .validateReturnValue(eventHandler, method, id);
 */