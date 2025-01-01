// 상단 링크 시작
const headerLogo = document.querySelector("#header-logo");
headerLogo.addEventListener("click", () => {
    window.location.href = "/";
})
// const headerRightSearch = document.querySelector("#header-right-search");
// headerRightSearch.addEventListener("click", () => {
//     window.location.href = "/";
// }) 상단 돋보기
const headerSignup = document.querySelector("#header-right-signup");
headerSignup.addEventListener("click", () => {
    window.location.href = "/";
})
const headerLogin = document.querySelector("#header-right-login");
headerLogin.addEventListener("click", () => {
    window.location.href = "/";
})
const headerCompanyService = document.querySelector("#header-right-company-services");
headerCompanyService.addEventListener("click", () => {
    window.location.href = "/";
})
// 상단 링크 끝
// dropdown 시작
const headerLeftMenuRecruitment = document.querySelector("#header-left-menu-recruitment");
const dropdown = document.querySelector("#dropdown");
headerLeftMenuRecruitment.addEventListener("mouseenter", () => {
    console.log("드롭다운 열기");
    dropdown.classList.remove("none");
    dropdown.classList.add("block");
});

headerLeftMenuRecruitment.addEventListener("mouseleave", () => {
    console.log("드롭다운 닫기");
    dropdown.classList.remove("block");
    dropdown.classList.add("none");
});

const dropdownMenus = document.querySelectorAll(".dropdown-menu");

dropdownMenus.forEach((menu) => {
    menu.addEventListener("mouseenter", () => {
        const subDropdown = menu.querySelector(".sub-dropdown");
        if (subDropdown) {
            subDropdown.classList.remove("none");
            subDropdown.classList.add("block");
        }
    });

    menu.addEventListener("mouseleave", () => {
        const subDropdown = menu.querySelector(".sub-dropdown");
        if (subDropdown) {
            subDropdown.classList.remove("block");
            subDropdown.classList.add("none");
        }
    });
});
// dropdown 끝
// 상단 돋보기 모달버전
const headerRightSearch = document.querySelector("#header-right-search");
const modal = document.querySelector("#modal");
const closeBtn = document.querySelector("#close-btn");

// 모달창 열기
headerRightSearch.addEventListener("click", () => {
    modal.style.display = "flex"; // 모달창 보이기
});

// 모달창 닫기
closeBtn.addEventListener("click", () => {
    modal.style.display = "none"; // 모달창 숨기기
});
// 상단 돋보기 모달 끝

