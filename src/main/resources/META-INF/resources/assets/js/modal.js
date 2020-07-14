$(document).ready(function() {
    $(".modal-form").on("click", function(event){
        var url = $(this).attr("href");
        var title = $("h2").text();

        Lobibox.window({
            title: title,
            content: function(){
                loadModal(url);
            },
            width: 440,
            height: 470,
            url: url,
            autoload: false,
            loadMethod: "POST",
            buttons: {
                load: {
                    text: 'Enviar'
                },
                close: {
                    text: 'Fechar',
                    closeOnClick: true
                }
            },
            callback: function($this, type, ev){
                if (type === 'load'){
                var formDataObj = {};
                (function(){
                    $("form").find(":input").not("[type='submit']").each(function(){
                    formDataObj[$(this).attr("name")] = $(this).val();
                    });
                })();
                var data = JSON.stringify(formDataObj);
                var validated = true;
                $("form").find("input[required=true], textarea[required=true]").each(function(){
                    if($(this).val() == ""){
                    $("#msg").text("O campo '" + $(this).attr('id') + "' é obrigatório!").addClass("alert alert-danger");
                    validated = false;
                    } 
                });
                if(validated)
                    submitted(url, data);
                }
            }
        });

        event.preventDefault();
    });
    
    function loadModal(url) {
        $.get(url, function(data, status){
            $(".lobibox-body").html(data);
        });
    }

    function submitted(url, data) {
        $.ajax({
        url: url,
        data: data,
        type: "POST",
        contentType: "application/json",
        headers: {
            "Authorization": "Bearer "+localStorage.getItem("token")
        },
        success: function(result) { 
            $(".lobibox-window").remove();
            $(".lobibox-backdrop").remove();
            $("#content").html(result);
            dateFormat();
            Lobibox.notify("success", {
            title: "Sucesso!",
            msg: "Cadastro salvo com sucesso.",
            sound: false,
            });
        },
        error: function() { 
            $(".lobibox-window").remove();
            $(".lobibox-backdrop").remove();
            Lobibox.notify("error", {
            title: "Erro!",
            msg: "Ocorreu um erro ou você não possui autorização para cadastrar.",
            sound: false,
            });
        }
        });
        return false;
    }

    $(".modal-view").on("click", function(event){
        var url = $(this).attr("href");
        var title = $("h2").text();

        Lobibox.window({
            title: title,
            content: function(){
                loadModal(url);
            },
            width: 440,
            height: 470
        });

        event.preventDefault();
    });
});