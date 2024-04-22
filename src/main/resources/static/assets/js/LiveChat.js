'use strict';

const emailPage = document.querySelector('#email-page');
const chatPage = document.querySelector('#chat-page');
const emailForm = document.querySelector('#emailForm');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const connectingElement = document.querySelector('.connecting');
const chatArea = document.querySelector('#chat-messages');
const logout = document.querySelector('#logout');

let stompClient = null;
let email = null;
let selectedUserId = null;


window.addEventListener('DOMContentLoaded', (event) => {
    email = document.getElementById('user').value
    connect(event);
});


function connect(event) {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
}


function onConnected() {
    stompClient.subscribe(`/user/${email}/queue/messages`, onMessageReceived);
    stompClient.subscribe(`/user/public`, onMessageReceived);

    // register the connected user
    stompClient.send("/app/user.addUser",
        {},
        JSON.stringify({email: email, status: 'ONLINE'})
    );
    findAndDisplayConnectedUsers().then();
}

async function findAndDisplayConnectedUsers() {
    const connectedUsersResponse = await fetch('/users');
    let connectedUsers = await connectedUsersResponse.json();
    connectedUsers = connectedUsers.filter(user => user.email !== email);
    const connectedUsersList = document.getElementById('connectedUsers');
    connectedUsersList.innerHTML = '';

    connectedUsers.forEach(user => {
        appendUserElement(user, connectedUsersList);
        if (connectedUsers.indexOf(user) < connectedUsers.length - 1) {
            const separator = document.createElement('li');
            separator.classList.add('separator');
            connectedUsersList.appendChild(separator);
        }
    });
}

function appendUserElement(user, connectedUsersList) {
    const listItem = document.createElement('li');
    listItem.classList.add('user-item');
    listItem.setAttribute('data-email', user.email);

    const emailSpan = document.createElement('span');
    emailSpan.textContent = user.name;

    const receivedMsgs = document.createElement('span');
    receivedMsgs.classList.add('nbr-msg', 'hidden');

    listItem.appendChild(emailSpan);
    listItem.appendChild(receivedMsgs);

    listItem.addEventListener('click', userItemClick);

    connectedUsersList.appendChild(listItem);
}

function userItemClick(event) {
    document.querySelectorAll('.user-item').forEach(item => {
        item.classList.remove('active');
    });
    messageForm.classList.remove('hidden');

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    selectedUserId = clickedUser.getAttribute('data-email');
    fetchAndDisplayUserChat().then();

    const nbrMsg = clickedUser.querySelector('.nbr-msg');
    nbrMsg.classList.add('hidden');

}

function displayMessage(senderId, content) {
    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');
    if (senderId === email) {
        messageContainer.classList.add('sender');
    } else {
        messageContainer.classList.add('receiver');
    }
    const message = document.createElement('p');
    message.textContent = content;
    messageContainer.appendChild(message);
    chatArea.appendChild(messageContainer);
}

async function fetchAndDisplayUserChat() {
    const userChatResponse = await fetch(`/messages/${email}/${selectedUserId}`);
    const userChat = await userChatResponse.json();
    chatArea.innerHTML = '';
    userChat.forEach(chat => {
        displayMessage(chat.senderId.email, chat.content);
    });
    chatArea.scrollTop = chatArea.scrollHeight;
}


function onError() {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    const messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        const chatMessage = {
            senderId: email,
            recipientId: selectedUserId,
            content: messageInput.value.trim(),
            timestamp: new Date()
        };
        stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
        displayMessage(email, messageInput.value.trim());
        messageInput.value = '';
    }
    chatArea.scrollTop = chatArea.scrollHeight;
    event.preventDefault();
}


async function onMessageReceived(payload) {
    await findAndDisplayConnectedUsers();
    const message = JSON.parse(payload.body);
    if (selectedUserId && selectedUserId === message.senderId.email) {
        displayMessage(message.senderId.email, message.content);
        chatArea.scrollTop = chatArea.scrollHeight;
    }

    const notifiedUser = document.querySelector(`[data-email="${message.senderId.email}"]`);
    if (notifiedUser && !notifiedUser.classList.contains('active')) {
        const nbrMsg = notifiedUser.querySelector('.nbr-msg');
        nbrMsg.classList.remove('hidden');
    }
}

function onLogout() {
    stompClient.send("/app/user.disconnectUser",
        {},
        JSON.stringify({email: email, status: 'OFFLINE'})
    );
    window.location.reload();
}

messageForm.addEventListener('submit', sendMessage, true);

// Use this to disconnect stom
window.onbeforeunload = function(e) {
    onLogout();
}
