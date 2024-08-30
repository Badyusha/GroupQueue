async function fetchData(requestText) {
    let response = await fetch(requestText);
    return await response.json();
}

function transformText(input) {
    // Convert the input to lowercase
    let lowerCaseText = input.toLowerCase();

    // Replace underscores with spaces
    let spacedText = lowerCaseText.replace(/_/g, ' ');

    // Capitalize the first letter
    return spacedText.charAt(0).toUpperCase() + spacedText.slice(1);
}

document.addEventListener('DOMContentLoaded', async function() {
    let requests = await fetchData('/request/become_group_admin/get');

    // fill in table header
    let requestsTable = document.getElementById('requests-table');
    let tableRowHeader = document.createElement('tr');

    if(requests.length === 0) {
        let requestsContainer = document.getElementById('requests');
        requestsContainer.innerText = "No requests found";

        requestsContainer.style.textAlign = 'center';
        requestsContainer.style.color = 'var(--inputText)';
        requestsContainer.style.padding = '1em';
        requestsContainer.style.fontWeight = '600';

        requestsTable.remove();
        return;
    }

    let lastNameHeader = document.createElement('th');
    lastNameHeader.innerText = "Last name";

    let firstNameHeader = document.createElement('th');
    firstNameHeader.innerText = "First name";

    let usernameHeader = document.createElement('th');
    usernameHeader.innerText = "Username";

    let roleTypeHeader = document.createElement('th');
    roleTypeHeader.innerText = "Role type";

    let groupNumberHeader = document.createElement('th');
    groupNumberHeader.innerText = "Group number";

    let requestTypeHeader = document.createElement('th');
    requestTypeHeader.innerHTML = "Request type";

    let actionHeader = document.createElement('th');
    actionHeader.innerHTML = "Action";

    tableRowHeader.appendChild(lastNameHeader);
    tableRowHeader.appendChild(firstNameHeader);
    tableRowHeader.appendChild(usernameHeader);
    tableRowHeader.appendChild(roleTypeHeader);
    tableRowHeader.appendChild(groupNumberHeader);
    tableRowHeader.appendChild(requestTypeHeader);
    tableRowHeader.appendChild(actionHeader);

    requestsTable.append(tableRowHeader);

    // fill in table body
    requests.forEach(request => {
        let tableRow = document.createElement('tr');

        let lastName = tableRow.insertCell(0);
        lastName.innerText = request.lastName;

        let firstName = tableRow.insertCell(1);
        firstName.innerText = request.firstName;

        let username = tableRow.insertCell(2);
        username.innerText = request.username;

        let roleType = tableRow.insertCell(3);
        roleType.innerText = request.roleType;

        let groupNumber = tableRow.insertCell(4);
        groupNumber.innerText = request.groupNumber;

        let requestType = tableRow.insertCell(5);
        requestType.innerText = transformText(request.requestType);

        let action = tableRow.insertCell(6);

        action.innerHTML = `
            <div class="action-type">
                <a class="accept-request" onclick="acceptRequest(${request.studentId}, '${request.requestType}')">Accept</a>
                <a class="decline-request" onclick="declineRequest(${request.studentId}, '${request.requestType}')">Decline</a>
            </div>
            `;

        requestsTable.append(tableRow);
    });
});

function acceptRequest(studentId, requestType) {
    $.ajax({
        type: 'POST',
        url: '/request/become_group_admin/accept',
        data: JSON.stringify({
            studentId: studentId,
            requestType: requestType
        }),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        success: async function () {
            window.location.reload();
        },
        error: async function (response) {
            console.error("Error while registration: " + response.data)
        }
    });
}

function declineRequest(studentId, requestType) {
    $.ajax({
        type: 'POST',
        url: '/request/become_group_admin/decline',
        data: JSON.stringify({
            studentId: studentId,
            requestType: requestType
        }),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        success: async function () {
            window.location.reload();
        },
        error: async function (response) {
            console.error("Error while declining request: /request/become_group_admin/decline\n" + response.data)
        }
    });
}