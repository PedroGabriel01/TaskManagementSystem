const baseUrl = "http://localhost:8080";

class Task {
    taskId;
    title;
    description;
    dtCreated;
    dtUpdated;
    taskStatus = new TaskStatus();
}

class TaskStatus {
    statusId;
    description;
}

let taskStatusSelected = new TaskStatus();
let taskSelected = new Task();

function executeFetch(fetch) {
    return new Promise((resolve, reason) => {
        fetch.then(async response => {
            if (!response.ok) {
                this.showAlert(await response.text());
            } else {
                return response.json();
            }
        }).then(data => {
            resolve(data);
        }).catch(e => {
            reason(e);
        })
    });
}

async function get(url) {
    const customFetch = fetch(url, { method: "GET" });
    return this.executeFetch(customFetch);
}

async function myDelete(url) {
    const customFetch = fetch(url, { method: "DELETE" });
    return this.executeFetch(customFetch);
}

async function post(url, body) {
    const customFetch = fetch(url, { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(body) });
    return this.executeFetch(customFetch);
}

function getNewPosition(column, posY) {
    const cards = column.querySelectorAll(".card:not(.dragging)");
    let result;

    for (let refer_card of cards) {
        const box = refer_card.getBoundingClientRect();
        const boxCenterY = box.y + box.height / 2;

        if (posY >= boxCenterY) result = refer_card;
    }

    return result;
}

function populateStatus(data) {
    const tasksPanel = document.getElementById("tasksPanel");
    tasksPanel.innerHTML = "";

    data.forEach(item => {
        const column = document.createElement("div");
        const cardsList = document.createElement("div");
        cardsList.classList.add("card-list");
        column.classList.add("column");
        column.classList.add("shadow");

        column.addEventListener("dragover", (e) => {
            const dragging = document.querySelector(".dragging");
            if (!dragging) return;

            const applyAfter = getNewPosition(cardsList, e.clientY);

            if (applyAfter) {
                applyAfter.insertAdjacentElement("afterend", dragging);
            } else {
                cardsList.prepend(dragging);
            }
            taskStatusSelected = item;
        });

        const title = document.createElement("h2");
        title.classList.add("column-title");
        title.textContent = item.description.toUpperCase();
        
        column.appendChild(title);
        column.appendChild(cardsList);
        tasksPanel.appendChild(column);
        this.getTasks(cardsList, item.statusId);
    });
}

function populateTasks(cardsList, data) {
    data.forEach(item => {
        const card = document.createElement("div");
        card.classList.add("card");
        card.setAttribute("draggable", true);

        card.addEventListener("dragstart", (e) => {
            taskSelected = item;
            e.target.classList.add("dragging");
        });

        card.addEventListener("dragend", (e) => {
            this.changeTaskStatus();
            taskSelected = new Task();
            e.target.classList.remove("dragging");
        });
        
        card.addEventListener("click", () => edit(item));

        const title = document.createElement("h4");
        title.classList.add("card-title");
        title.textContent = item.title;
        const description = document.createElement("p");
        description.classList.add("card-description");
        description.textContent = item.description;

        card.appendChild(title);
        card.appendChild(description);
        cardsList.appendChild(card);
    });
}

async function getTasks(div, statusId) {
    const search = document.getElementById("search").value;
    let url = `${baseUrl}/task/status/${statusId}`;
    if (search) {
        url += `/${search}`;
    }
    this.populateTasks(div, await get(url));
}

async function getStatus() {
    const url = `${baseUrl}/status`;
    this.populateStatus(await get(url));
}

async function deleteTask() {
    const url = `${baseUrl}/task/${taskSelected.taskId}`;
    myDelete(url).finally(() => {
        getStatus();
    });
    hideModal();
}

let addModal;
let deleteButton;

document.addEventListener("DOMContentLoaded", (event) => {
    getStatus();

    const refresh = document.getElementById("refresh");
    refresh.addEventListener("click", () => getStatus());

    const add = document.getElementById("add");
    add.addEventListener("click", () => showModal());

    addModal = document.getElementById("addModal");
    addModal.addEventListener("click", (e) => {
        if (e.target.id == "addModal") {
            this.hideModal();
        }
    });

    const save = document.getElementById("save");
    save.addEventListener("click", () => this.save());

    const alertButton = document.getElementById("alertButton");
    alertButton.addEventListener("click", () => this.hideAlert());

    deleteButton = document.getElementById("delete");
    deleteButton.addEventListener("click", () => this.deleteTask());
});

async function save() {
    const title = document.getElementById("title").value;
    const description = document.getElementById("description").value;

    taskSelected.title = title;
    taskSelected.description = description;

    const url = `${baseUrl}/task`;
    await post(url, taskSelected);
    getStatus();
    hideModal();
}

async function changeTaskStatus() {
    if (taskStatusSelected.statusId != taskSelected.taskStatus.statusId) {
        taskSelected.taskStatus = taskStatusSelected;
        const url = `${baseUrl}/task`;
        post(url, taskSelected).finally(() => getStatus());
    }
}

function showModal() {
    addModal.classList.remove('hidden');
    addModal.classList.add('visible');
    if (taskSelected.taskId) {
        deleteButton.style.display = null;
    } else {
        deleteButton.style.display = "none";
    }
}

function hideModal() {
    addModal.classList.add('hidden');
    addModal.classList.remove('visible');
    setTimeout(() => {
        cleanFields();
    }, 100);
}

function cleanFields() {
    taskSelected = new Task();
    document.getElementById("title").value = "";
    document.getElementById("description").value = "";
}

function edit(item) {
    taskSelected = item;
    document.getElementById("title").value = taskSelected.title;
    document.getElementById("description").value = taskSelected.description;
    showModal();
}

function showAlert(message) {
    const alertModal = document.getElementById("alertModal");

    const description = document.getElementById("alertMessage");
    description.textContent = message;

    visible(alertModal);
}

function hideAlert() {
    const alertModal = document.getElementById("alertModal");
    hidden(alertModal);
}

function visible(element) {
    element.classList.remove('hidden');
    element.classList.add('visible');
}

function hidden(element) {
    element.classList.add('hidden');
    element.classList.remove('visible');
}