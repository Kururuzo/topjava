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

    //todo to closest, like https://stackoverflow.com/questions/10499435/parent-vs-closest
    let checkBoxTrParent = $(checkBox.parent().parent());

    if (checkBox[0].checked == true){
        checkBoxTrParent.attr("data-isActive", "true")

        // alert('checked');
    } else {
        checkBoxTrParent.attr("data-isActive", "false")

        // console.log('checkBox = %s, checkBox.checked = %s, checkBoxTrParent = %s',
        //     checkBox, checkBox.checked, checkBoxTrParent[0])
    }
};