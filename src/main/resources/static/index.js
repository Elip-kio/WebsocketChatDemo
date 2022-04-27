function connect() {
    try {
        return new WebSocket(`ws://${window.location.host}/chat-service`)
    } catch (e) {
        console.error(e);
        alert("Unable to connect chat service!")
    }
}

function setNickname(ws, name) {
    ws.send(JSON.stringify({
        type: "setNickname",
        nickname: name
    }))
}

const roomData = {
    currentUser: "",
    users: [],
    messages: []
}

function sendMessage(ws, message) {
    ws.send(JSON.stringify({
        type: "sendMessage",
        message: message
    }))
}

function onInit(data) {
    document.querySelector("#launch").classList.remove("active")
    document.querySelector("#chat").classList.add("active")

    roomData.users = data.users
    roomData.currentUser = data.currentUser
    roomData.messages = data.messages
    refreshUI()

    document.querySelector("#send").addEventListener("click", () => {
        sendMessage(ws, document.querySelector("#msgInput").value)
        document.querySelector("#msgInput").value = ""
    })
}

function onUserEnter(data) {
    roomData.users.push(data.user)
    refreshUI()
}

function onUserLeave(data) {
    roomData.users = roomData.users.filter((item) => item !== data.user)
    refreshUI()
}

function onNewMessage(data) {
    roomData.messages.push(data.message)
    refreshUI()
}

function refreshUI() {
    document.querySelector("#userListTitle").innerHTML = `👷‍♀️ Online Users（${roomData.users.length || 0}）`
    document.querySelector("#meTitle").innerHTML = `🌍 ${roomData.currentUser}`

    let userListHtml = ""
    roomData.users.forEach(item => {
        userListHtml += `<li>${item}</li>`
    })
    document.querySelector("#userList").innerHTML = userListHtml

    let messageListHtml = ""
    roomData.messages.forEach(item => {
        if (!item) return
        messageListHtml += `
        <li>
        <div class="p-4 rounded my-4 ml-4 inline-block"
            style="background-color: #97b4e9; line-height: 30px; max-width: 80%;">
            <span class="text-white font-bold px-2 py-1 rounded"
                style="background-color:#ff6348">${item.user}</span>
            <span>&nbsp;</span>
            <span class="text-md">${item.content}</span>
            <p class="text-sm text-right text-gray-600 pt-2">${item.date}</p>
        </div>
        </li>
        `
    })
    const eleMsgList = document.querySelector("#msgList")
    eleMsgList.innerHTML = messageListHtml
    setTimeout(() => {
        eleMsgList.scrollTo(0, eleMsgList.scrollHeight)
    }, 100)

}

const ws = connect()

ws.onopen = () => {
    document.querySelector("#loading").classList.add("hidden")
    document.querySelector("#launch").classList.add("active")

    document.querySelector("#getStart").addEventListener("click", () => {
        setNickname(ws, document.querySelector("#nickname").value)
    })
}

ws.onmessage = (data) => {
    const resp = JSON.parse(data.data)
    console.log(resp);
    switch (resp.type) {
        case "init":
            onInit(resp);
            break;
        case "userEnter":
            onUserEnter(resp);
            break;
        case "userLeave":
            onUserLeave(resp);
            break;
        case "newMessage":
            onNewMessage(resp);
            break;
    }
}