async function fetchData(requestText) {
    let response = await fetch(requestText);
    return await response.json();
}

document.addEventListener('DOMContentLoaded', async function() {
    let requests = await fetchData('/request/become_group_admin/get');

    // fill in table header
    let requestsTable = document.getElementById('requests-table');
    let tableRowHeader = document.createElement('tr');

    if(requests.length === 0) {
        let noDataHeader = document.createElement('th');
        noDataHeader.innerText = "No data found";
        tableRowHeader.appendChild(noDataHeader);

        noDataHeader.style.textAlign = 'center';
        noDataHeader.style.color = 'var(--inputText)';

        requestsTable.append(tableRowHeader);
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
        requestType.innerText = request.requestType;

        let action = tableRow.insertCell(6);
        action.innerHTML = `
            <div class="action-type">
                <a class="accept-request" onclick="acceptRequest(${request.userId}, '${request.requestType}')">Accept</a>
                <a class="decline-request" onclick="declineRequest(${request.userId}, '${request.requestType}')">Decline</a>
            </div>
            `;

        requestsTable.append(tableRow);
    });
});

function acceptRequest(userId, requestType) {
    $.ajax({
        type: 'POST',
        url: '/request/become_group_admin/accept',
        data: JSON.stringify({
            userId: userId,
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

function declineRequest(userId, requestType) {
    $.ajax({
        type: 'POST',
        url: '/request/become_group_admin/decline',
        data: JSON.stringify({
            userId: userId,
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