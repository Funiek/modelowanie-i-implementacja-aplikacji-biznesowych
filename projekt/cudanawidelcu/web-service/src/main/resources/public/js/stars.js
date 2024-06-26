﻿let starContainer = document.querySelectorAll(".star-container");
const submitButton = document.querySelector("#submit");
const message = document.querySelector("#message");
const submitSection = document.querySelector("#submit-section");
let activeElements = []

let events = {
    mouse: {
        over: "click",
    },
    touch: {
        over: "touchstart",
    },
};

let deviceType = "";

const isTouchDevice = () => {
    try {
        document.createEvent("TouchEvent");
        deviceType = "touch";
        return true;
    } catch (e) {
        deviceType = "mouse";
        return false;
    }
};

isTouchDevice();

starContainer.forEach((element, index) => {
    element.addEventListener(events[deviceType].over, () => {
        submitButton.disabled = false;
        if (element.classList.contains("inactive")) {
            ratingUpdate(0, index, true);
        } else {
            ratingUpdate(index, starContainer.length - 1, false);
        }
    });
});

const ratingUpdate = (start, end, active) => {
    for (let i = start; i <= end; i++) {
        if (active) {
            starContainer[i].classList.add("active");
            starContainer[i].classList.remove("inactive");
            starContainer[i].firstElementChild.className = "fa-star fa-solid";
        } else {
            starContainer[i].classList.remove("active");
            starContainer[i].classList.add("inactive");
            starContainer[i].firstElementChild.className = "fa-star fa-regular";
        }
    } 
    
};

submitButton.addEventListener("click", () => {
    submitSection.classList.remove("hide");
    submitSection.classList.add("show");
    submitButton.disabled = true;

    let activeElements = document.getElementsByClassName("active");

    let postData = {
        recipeId: recipeId,
        rating: activeElements.length
    }

    $.ajax({
        type: "POST",
        url: window.location.protocol + "//" + window.location.host + "/votes/rating",
        contentType: "application/json",
        data: JSON.stringify(postData),
        success: function(data, status) {
            $('#dd-rating').text(data.rating.toFixed(2));
            $('#dd-count-votes').text(data.countVotes);
        },
        error: function(xhr, status, error) {
            console.error("Wystąpił błąd podczas wysyłania żądania: " + status + "Error: " + error);
        }
    });


});

window.onload = () => {
    submitButton.disabled = true;
    submitSection.classList.add("hide");
};