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
    let response = await fetchData('/student/get/info');

    username = response.username;
    firstName = response.firstName;
    lastName = response.lastName;
    groupNumber = response.groupNumber;
    let roleType = response.roleType;

    document.getElementById('full-name').innerHTML = `${firstName} ${lastName}`;
    document.getElementById('username').innerHTML = `${username}`;
    document.getElementById('group-number').innerHTML = `Group: ${groupNumber}`;
    document.getElementById('role-type').innerHTML = `Role: ${roleType}`;

    let editProfile = `<div id="edit-profile" class="menu-variant">
                                    <i class="fa-solid fa-user-large"></i>
                                    <label class="menu-label">Edit profile</label>
                                </div>`;
    let userQueues = `<div id="user-queues" class="menu-variant">
                                    <i class="fa-solid fa-layer-group"></i>
                                    <label class="menu-label">Your queues</label>
                                </div>`;
    let horizontalLine = `<div class="horizontal-line"></div>`;
    let becomeGroupAdmin = `<div id="become-group-admin-variant" class="menu-variant">
                                        <i class="fa-solid fa-hand"></i>
                                        <label id="become-group-admin" class="menu-label">Become group admin</label>
                                    </div>`;
    let chooseSortType = `<div id="sort-type" class="menu-variant">
                                        <i class="fa-solid fa-arrow-up-wide-short"></i>
                                        <label id="sort-type-label" class="menu-label">Choose sort type</label>
                                    </div>`;
    let groupAdminRequests = `<div id="become-admin-requests" class="menu-variant">
                                        <i class="fa-solid fa-handshake"></i>
                                        <label id="become-admin-requests-label" class="menu-label">Group admin requests</label>
                                    </div>`;
    let signOutDiv = `<div id="sign-out" class="menu-variant">
                                <i class="fa-solid fa-right-from-bracket"></i>
                                <label class="menu-label">Sign out</label>
                            </div>`;

    let sideMenuActions = document.getElementById('side-menu-actions');
    let sideMenuDiv = editProfile + userQueues + horizontalLine;

    if(roleType === 'USER') {
        sideMenuDiv += becomeGroupAdmin;
    } else {
        sideMenuDiv += chooseSortType;
    }
    if(roleType === 'SUDO') {
        sideMenuDiv += groupAdminRequests;
    }
    sideMenuDiv += horizontalLine + signOutDiv;

    sideMenuActions.innerHTML = sideMenuDiv;


    addEventListeners(roleType);
}

function addEventListeners(roleType) {
    const showUserQueuesButton = document.getElementById('user-queues');
    showUserQueuesButton.addEventListener('click', function () {
        window.location.href = '/student/queues';
        sideMenu.classList.remove('active');
        menuOverlay.classList.remove('active');
    });

    const editProfileButton = document.getElementById('edit-profile');
    editProfileButton.addEventListener('click', async function() {
        fillProfileInputs();

        darkOverlay.classList.toggle('active');
        sideMenu.classList.remove('active');
        menuOverlay.classList.remove('active');
        editProfileForm.style.display = 'block';
    });

    const signOut = document.getElementById('sign-out');
    signOut.addEventListener('click', function() {
        window.location.replace('/');
    });

    let userRoleType = 'USER';
    let sudoRoleType = 'SUDO';
    if(roleType === userRoleType) {
        document.getElementById('become-group-admin-variant').addEventListener('click', function () {
            sendBecomeGroupAdminRequest(userRoleType);
        })
    } else {
        document.getElementById('sort-type').addEventListener('click', function() {
            chooseSortType(userRoleType);
        })
    }
    if(roleType === sudoRoleType) {
        document.getElementById('become-admin-requests').addEventListener('click', function() {
            showBecomeAdminRequests(sudoRoleType);
        })
    }
}

async function sendBecomeGroupAdminRequest(roleType) {
    let isUserRoleCorrect = await fetchData(`/student/${roleType}/is_student_role`);
    if(!isUserRoleCorrect) {
        return;
    }
    sideMenu.classList.remove('active');
    menuOverlay.classList.remove('active');
    overlay.classList.toggle('active');
    becomeGroupAdminForm.style.display = 'block';
}

async function chooseSortType(roleType) {
    let isUserRoleIncorrect = await fetchData(`/student/${roleType}/is_student_role`);
    if(isUserRoleIncorrect) {
        return;
    }
    window.location.href = '/lesson/choose/sort_type';
    sideMenu.classList.remove('active');
    menuOverlay.classList.remove('active');
}

async function showBecomeAdminRequests(roleType) {
    let isUserRoleCorrect = await fetchData(`/student/${roleType}/is_student_role`);
    if(!isUserRoleCorrect) {
        return;
    }
    window.location.href = '/request/become_group_admin';
    sideMenu.classList.remove('active');
    menuOverlay.classList.remove('active');
}