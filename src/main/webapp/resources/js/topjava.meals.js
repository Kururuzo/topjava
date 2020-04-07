// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: "ajax/meals/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
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
                        "desc"
                    ]
                ]
            }),
            updateTable: filter
        }
    );
});


function filter() {
    $.ajax({
        type: "GET",
        url: "ajax/meals/filter",
        data: $('#filterForm').serialize()
    }).done(reload); // in topjava.common.js
}

function cleanFilter() {
    //https://qna.habr.com/q/214378
    $('#filterForm')[0].reset();

    $.ajax({
        type: "GET",
        url: "ajax/meals/",
    }).done(reload); // in topjava.common.js
    successNoty("Filter cleared");
}