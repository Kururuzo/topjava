// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: "ajax/admin/users/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "name"
                    },
                    {
                        "data": "email"
                    },
                    {
                        "data": "roles"
                    },
                    {
                        "data": "enabled"
                    },
                    {
                        "data": "registered"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "asc"
                    ]
                ]
            }),
        updateTable: filter
        }
    );
    // updateTable: filter
});

function filter() {
    $.get(context.ajaxUrl, function (data) {
        reload(data);
    });
}

function isActive(userId) {
    let checkBox = $('#' + userId);
    let checkBoxStatus = checkBox[0].checked; //its a new status of checkbox !

    //todo to closest, like https://stackoverflow.com/questions/10499435/parent-vs-closest
    let checkBoxTrParent = $(checkBox.parent().parent());

    //Must be Patch, but don't work. Why??
    $.ajax({
        url: "ajax/admin/users/" + userId,
        type: "POST",
        data: "checkBoxStatus=" + checkBoxStatus
    }).done(function () {
        checkBoxTrParent.attr("data-isActive", checkBoxStatus);
        // console.log('checkBoxStatus = %s', checkBoxStatus);
        successNoty("Status changed");
    });
};