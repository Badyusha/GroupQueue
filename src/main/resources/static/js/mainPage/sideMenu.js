const topRightDiv = document.getElementById('top-right-div');
const closeMenu = document.getElementById('close-menu');
const sideMenu = document.getElementById('side-menu');
const menuOverlay = document.getElementById('menu-overlay');

let firstName = '';
let lastName = '';
let username = '';
let groupNumber = 0;

topRightDiv.addEventListener('click', function() {
    sideMenu.classList.toggle('active');
    menuOverlay.classList.toggle('active');
});

closeMenu.addEventListener('click', function() {
    sideMenu.classList.remove('active');
    menuOverlay.classList.remove('active');
});

menuOverlay.addEventListener('click', function() {
    sideMenu.classList.remove('active');
    menuOverlay.classList.remove('active');
});



async function fillSideMenu() {
    let response = await fetchData('/user/get_info');

    let roleType = response.roleType;
    if(roleType === 'GROUP_ADMIN' || roleType === 'SUDO') {
        document.getElementById('become-group-admin-variant').remove();
        document.getElementById('become-group-admin-horizontal-line').remove();
    }

    username = response.username;
    firstName = response.firstName;
    lastName = response.lastName;
    groupNumber = response.groupNumber;

    document.getElementById('full-name').innerHTML = `${firstName} ${lastName}`;
    document.getElementById('username').innerHTML = `${username}`;
    document.getElementById('group-number').innerHTML = `Group: ${groupNumber}`;
    document.getElementById('role-type').innerHTML = `Role: ${roleType}`;
}

