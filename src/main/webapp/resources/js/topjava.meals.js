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
            })
        }
    );
    filter();
});


function filter() {
    $.ajax({
        type: "GET",
        url: "ajax/meals/filter",
        data: $('#filterForm').serialize(),
        cache: false,
        dataType: 'json',
    }).done(reload);

    function reload(data) {
        context.datatableApi.clear().rows.add(data).draw();
    }
}


function cleanFilter() {
    // why don't work???
    //https://qna.habr.com/q/214378
    // $('#filterForm')[0].reset();

    $('input')[0].value = '';
    $('input')[1].value = '';
    $('input')[2].value = '';
    $('input')[3].value = '';
    $.ajax({
        type: "GET",
        url: "ajax/meals/",
    }).done(function (data) {
        context.datatableApi.clear().rows.add(data).draw();
    });
    successNoty("Filter cleared");
}