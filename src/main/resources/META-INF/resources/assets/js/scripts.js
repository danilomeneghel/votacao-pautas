$(document).ready(function() {
    if(localStorage.getItem("token") != null) {
        $(".username").text(localStorage.getItem("username"));
        var page = (location.pathname == "/users/perfil/edit") ? "/users/perfil" : location.pathname;
        const queryString = location.search;
        const urlParams = new URLSearchParams(queryString);
        var filter = urlParams.get("filter");

        if(page != "/" && filter == null) {
            $.ajax({
                url: page+"/content",
                type: "GET",
                cache: false,
                contentType: "application/json",
                headers: {
                    "Authorization": "Bearer "+localStorage.getItem("token")
                },
                success: function(result) { 
                    $("#content").html(result);
                    dateFormat();
                },
                error: function() { 
                    $("#content").text("Erro ao carregar ou você não possui autorização para visualizar.");
                }
            });
        } else if(filter != null) {
            $.ajax({
                url: page+"/content",
                type: "GET",
                data: "filter="+filter,
                cache: false,
                contentType: "application/json",
                headers: {
                    "Authorization": "Bearer "+localStorage.getItem("token")
                },
                success: function(result) { 
                    $("#content").html(result);
                    dateFormat();
                },
                error: function() { 
                    $("#content").text("Erro ao carregar ou você não possui autorização para visualizar.");
                }
            });
        }
    } else {
        $("#content").html("Você não possui autorização para visualizar.");
    }
});

function logoff() {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    localStorage.removeItem("cpf");
    location.reload("/auth");
}

function dateFormat() {
    $(".date").each(function() {
        var dateText = $.trim($(this).text());
        var value = new Date(dateText).toLocaleDateString("pt-br", {timeZone: "UTC"});
        $(this).text(value);
    });
}