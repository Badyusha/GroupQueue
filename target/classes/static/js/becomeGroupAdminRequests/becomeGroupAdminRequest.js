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
    let requests = await fetchData('/request/get/become_group_admin');

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

    const headers = [
        { text: 'Last name', index: 0 },
        { text: 'First name', index: 1 },
        { text: 'Username', index: 2 },
        { text: 'Role type', index: 3 },
        { text: 'Group number', index: 4 },
        { text: 'Request type', index: 5 }
    ];

    headers.forEach(header => {
        let th = document.createElement('th');
        th.innerHTML = `${header.text}`;
        th.innerHTML = `${header.text} <span class="arrow">&#9650;</span><span class="arrow">&#9660;</span>`
        th.style.cursor = 'pointer';

        th.addEventListener('click', () => sortTable(header.index, th));
        tableRowHeader.appendChild(th);
    });

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

    function sortTable(columnIndex, headerElement) {
        let rows = Array.from(requestsTable.querySelectorAll('tr')).slice(1); // exclude header row
        let isAscending = headerElement.getAttribute('data-sort-direction') === 'asc';

        rows.sort((rowA, rowB) => {
            let cellA = rowA.cells[columnIndex].innerText.trim();
            let cellB = rowB.cells[columnIndex].innerText.trim();

            let comparison = 0;

            //          { text: 'Last name', index: 0 },
            //         { text: 'First name', index: 1 },
            //         { text: 'Username', index: 2 },
            //         { text: 'Role type', index: 3 },
            //         { text: 'Group number', index: 4 },
            //         { text: 'Request type', index: 5 }

            if (columnIndex === 4) { // Group number column
                comparison = Number.parseInt(cellA) - Number.parseInt(cellB);
            } else { // Other
                comparison = cellA.localeCompare(cellB);
            }

            return isAscending ? comparison : -comparison;
        });

        // Toggle sort direction
        isAscending = !isAscending;
        headerElement.setAttribute('data-sort-direction', isAscending ? 'asc' : 'desc');

        // Update arrow visibility
        headerElement.querySelectorAll('.arrow').forEach(arrow => arrow.style.color = 'var(--headerBorder)');
        if (isAscending) {
            headerElement.querySelector('.arrow:last-child').style.color = 'var(--gray)';
        } else {
            headerElement.querySelector('.arrow:first-child').style.color = 'var(--gray)';
        }

        rows.forEach(row => requestsTable.appendChild(row));
    }
});

function acceptRequest(studentId, requestType) {
    $.ajax({
        type: 'POST',
        url: '/request/accept/become_group_admin',
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
            console.error("Error while accepting request: /request/accept/become_group_admin\n" + response.data)
        }
    });
}

function declineRequest(studentId, requestType) {
    $.ajax({
        type: 'POST',
        url: '/request/decline/become_group_admin',
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
            console.error("Error while declining request: /request/decline/become_group_admin\n" + response.data)
        }
    });
}