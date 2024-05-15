const pdfButton = document.querySelector("#pdfButton");


pdfButton.addEventListener("click", () => {
    $.post("https://localhost:7236/Recipe/Pdf",
        {
            Name: recipeName,
        },
        function (data, status) {
            let link = document.createElement("a");
            link.download = recipeName + ".pdf";
            link.href = 'https://localhost:3333/pdf/' + recipeName + ".pdf";
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            delete link;
        }
    );
});