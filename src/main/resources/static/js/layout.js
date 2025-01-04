// 상단 링크 시작
const headerLogo = document.querySelector("#header-logo");
headerLogo.addEventListener("click", () => {
    window.location.href = "/";
});

const headerSignup = document.querySelector("#header-right-signup");
headerSignup.addEventListener("click", () => {
    window.location.href = "/";
});

const headerLogin = document.querySelector("#header-right-login");
headerLogin.addEventListener("click", () => {
    window.location.href = "/";
});

const headerCompanyService = document.querySelector("#header-right-company-services");
headerCompanyService.addEventListener("click", () => {
    window.location.href = "/";
});
// 상단 링크 끝

// dropdown 시작
const headerLeftMenuRecruitment = document.querySelector("#header-left-menu-recruitment");
const headerDropdown = document.querySelector("#header-dropdown");

headerLeftMenuRecruitment.addEventListener("mouseenter", () => {
    headerDropdown.classList.remove("none");
    headerDropdown.classList.add("block");
});

headerLeftMenuRecruitment.addEventListener("mouseleave", () => {
    headerDropdown.classList.remove("block");
    headerDropdown.classList.add("none");
});

const headerDropdownMenus = document.querySelectorAll(".header-dropdown-menu");

headerDropdownMenus.forEach((menu) => {
    menu.addEventListener("mouseenter", () => {
        const subDropdown = menu.querySelector(".header-sub-dropdown");
        if (subDropdown) {
            subDropdown.classList.remove("none");
            subDropdown.classList.add("block");
        }
    });

    menu.addEventListener("mouseleave", () => {
        const subDropdown = menu.querySelector(".header-sub-dropdown");
        if (subDropdown) {
            subDropdown.classList.remove("block");
            subDropdown.classList.add("none");
        }
    });
});
// dropdown 끝


