const topRightDiv = document.getElementById('top-right-div');
const closeMenu = document.getElementById('close-menu');
const sideMenu = document.getElementById('side-menu');
const menuOverlay = document.getElementById('menu-overlay');

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