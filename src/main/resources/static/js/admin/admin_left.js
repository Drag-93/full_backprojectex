document.addEventListener("DOMContentLoaded", () => {
    const openApiBtn = document.querySelector(".open-api-btn");

    if (!openApiBtn) return;

    openApiBtn.addEventListener("click", (event) => {
        event.preventDefault();

        const openMenu = openApiBtn.closest(".open");
        openMenu.classList.toggle("active");
    });
});