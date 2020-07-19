$(document).ready(function() {
    $(".modal-form").on("click", function(event){
        var url = $(this).attr("href");
        var title = $("h2").text();

        Lobibox.window({
            title: title,
            content: function(){ loadModal(url); },
            width: 440,
            height: 470,
            url: url,
            autoload: false,
            loadMethod: "POST",
            buttons: {
                load: { text: 'Enviar' },
                close: {
                    text: 'Fechar',
                    closeOnClick: true
                }
            },
            callback: function($this, type, ev){
                if (type === 'load'){
                    var urlSubmit = ($("#votacao").val() != undefined) ? "/votacao/votar" : url;
                    var formDataObj = {};
                    (function(){
                        $("form").find(":input").not("[type='submit']").each(function(){
                            if($(this).attr("type") == "radio" || $(this).attr("type") == "checkbox") 
                                formDataObj[$(this).attr("name")] = $("input[name='"+$(this).attr("name")+"']:checked").val();
                            else
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
                        submitted(urlSubmit, data);
                }
            }
        });

        event.preventDefault();
    });
    
    function loadModal(url) {
        $.get(url, function(data){
            $(".lobibox-body").html(data);
        });
    }

    function submitted(url, data) {
        $(".lobibox-body").html("<div class='alert alert-warning'>Carregando...</div>");
        $.ajax({
            url: url,
            data: data,
            type: "POST",
            contentType: "application/json",
            headers: { "Authorization": "Bearer "+localStorage.getItem("token") },
            success: function(result) { 
                $(".lobibox-window").remove();
                $(".lobibox-backdrop").remove();
                Lobibox.notify(result.type, {
                    title: result.title,
                    msg: result.description,
                    sound: false
                });

                $.ajax({
                    url: location.href+"/content",
                    type: "GET",
                    contentType: "application/json",
                    headers: { "Authorization": "Bearer "+localStorage.getItem("token") },
                    success: function(result) {
                        $("#content").html(result);
                        dateFormat();
                    }
                });
            },
            error: function() { 
                $(".lobibox-window").remove();
                $(".lobibox-backdrop").remove();
                Lobibox.notify("error", {
                    title: "Erro!",
                    msg: "Ocorreu um erro ou você não possui autorização para cadastrar.",
                    sound: false
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
                return "<div class='alert alert-warning'>Carregando...</div>";
            },
            width: 440,
            height: 470
        });

        event.preventDefault();
    });

    $(".modal-delete").on("click", function(event){
        var url = $(this).attr("href");

        Lobibox.confirm({
            title: "Alerta",
            msg: "Você tem certeza que deseja excluir esse item?",
            buttons: {
                yes: { text: 'Sim' },
                no: { text: 'Não' }
            },
            callback: function($this, type, ev){
                if (type === 'yes')
                    submitted(url, null);
            }
        });

        event.preventDefault();
    });
});