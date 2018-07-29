$(document).ready(function () {

    $('.grid').masonry({
        // set itemSelector so .grid-sizer is not used in layout
        itemSelector: '.grid-item',
        // use element for option
        columnWidth: '.grid-sizer',
        percentPosition: true
    });

    $("#toggle-holarse-context-menu").click(function (e) {
        e.preventDefault();
        $(".holarse-context-menu").slideToggle("slow");
    });
    
    $("#commentForm").submit(function(evt) {
        evt.preventDefault();
        var url = $(this).attr("action");
        var content = $("#commentForm").find("textarea[name='content']").val();
        
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        
        $.post({
            url: url,
            beforeSend: function(xhr) {
                // here it is
                xhr.setRequestHeader(header, token);
            },            
            data: { content: content },
            success: function(res) {
                $("#commentForm").find("textarea[name='content']").val("");
            }
        });
    });
       
}
);
