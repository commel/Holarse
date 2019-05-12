var holarse = {};
holarse.csrf_token  = $("meta[name=_csrf]").attr("content");
holarse.csrf_header = $("meta[name=_csrf_header]").attr("content");

// Kommentarsektion
var vcomments = new Vue({
    el: '#v-comments-1',
    data: {
        comments: []
    },
    mounted: function () {
        var nodeId = $("article").data("nodeid");
        if (nodeId === undefined) { return false; }
        console.debug("Loading comments for nodeid " + nodeId);
        $.getJSON("/nodes/comments", { nodeId: nodeId }, function (data) {
            vcomments.comments = data;
        });
    }
});

var vcommenteditor = new Vue({
    el: "#v-comment-editor",
    data: {
        newcomment: {
            nodeId: 0,
            content: ""
        }
    },
    mounted: function() {
        var nodeId = $("#v-comment-editor").data("nodeid");
        if (nodeId === undefined) { return false; } 
        
        this.newcomment.nodeId = nodeId;
    },
    methods: {
        submit: function(event) {
            event.preventDefault();
            $.ajax({
                url: '/nodes/comments',
                type: 'post',
                data: this.newcomment,
                beforeSend: function(request) {
                    request.setRequestHeader(holarse.csrf_header, holarse.csrf_token);
                },
                dataType: 'json',
                success: function(result) {
                    console.debug(result);
                    vcommenteditor.newcomment.content = "";
                    vcomments.$mount();
                }
            });
        }
    }    
});

var varticleeditor = new Vue({
    el: "#v-article-editor",
    data: {
        node: {}
    },
    mounted: function () {
        this.load();
    }, 
    methods: {
        load: function() {
        var nodeId = $("#v-article-editor").data("nodeid");
        if (nodeId === undefined || nodeId === "") { return false; }
        console.debug("Loading article data for nodeid " + nodeId);
        
            $.getJSON("/wiki/" + nodeId + "/edit.json", function (data) {
                varticleeditor.node = data;
            });            
        },
        submit: function(event) {
            event.preventDefault();
            $.ajax({
                url: '/wiki/' + this.node.nodeId,
                type: 'post',
                data: this.node,
                beforeSend: function(request) {
                    request.setRequestHeader(holarse.csrf_header, holarse.csrf_token);
                },
                dataType: 'json',
                success: function(result) {
                    console.debug(result);
                }
            });
        }
    }
});

