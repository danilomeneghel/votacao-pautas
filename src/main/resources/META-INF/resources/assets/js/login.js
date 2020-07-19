$(document).ready(function() {
    $("#card").flip({
        trigger: 'manual'
    });
    
    $("#flip-btn").click(function(){
        $("#card").flip(true);
    });
    $("#unflip-btn").click(function(){
        $("#card").flip(false);
    });

    if($(".alert-danger").text() != "")
        $("#card").flip(true);

    window.history.pushState("", "", "/auth");
    $("#loginForm").submit(function(event){
        var data = {
            username: $("#user").val(),
            password: $("#pass").val()
        };

        $.ajax({
            url: location.href+"/logar",
            data: JSON.stringify(data),
            type: "POST",
            cache: false,
            contentType: "application/json",
            beforeSend: function() {
                if($("#user").val() != null && $("#pass").val() != null)
                    $("#alert").text("Carregando...").removeClass().addClass("alert alert-warning");
            },
            success: function(result) { 
                if(!result) {
                    $("#alert").text("Usuário ou senha inválido.").addClass("alert alert-danger");
                    $(".alert-success").remove();
                    return false;
                } else {
                    localStorage.setItem("token", result.token);
                    localStorage.setItem("username", result.username);
                    localStorage.setItem("cpf", result.cpf);
                }
                $.ajax({
                    url: "/",
                    type: "GET",
                    cache: false,
                    contentType: "application/json",
                    headers: {
                        "Authorization": "Bearer "+result.token
                    },
                    success: function(data) { 
                        location.replace("/");
                    },
                    error: function() { 
                        $("#error").text("Erro ao tentar abrir a página. Tente novamente.").addClass("alert alert-danger");
                        $(".alert-success").remove();
                    },
                });
            },
            error: function() { 
                $("#error").text("Erro ao tentar logar. Tente novamente.").addClass("alert alert-danger");
                $(".alert-success").remove();
            }
        });
        event.preventDefault();
    });
});