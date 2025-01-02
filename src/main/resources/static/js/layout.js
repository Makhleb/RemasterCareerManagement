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

// 상단 돋보기 모달버전
const headerRightSearch = document.querySelector("#header-right-search");
const headerModal = document.querySelector("#header-modal");
const headerCloseBtn = document.querySelector("#header-close-btn");

// 모달창 열기
headerRightSearch.addEventListener("click", () => {
    headerModal.style.display = "flex"; // 모달창 보이기
});

// 모달창 닫기
headerCloseBtn.addEventListener("click", () => {
    headerModal.style.display = "none"; // 모달창 숨기기
});

// 검색 기능
const headerSearchInput = document.querySelector("#header-search-input");
const headerModalSearchButton = document.querySelector("#header-modal-search");

headerModalSearchButton.addEventListener("click", () => {
    const keyword = headerSearchInput.value.trim();
    headerModalSearchButton.addEventListener("click", () => {
        const keyword = headerSearchInput.value.trim();
        if (keyword) {
            window.location.href = `/view/users/job-post/list?keyword=${encodeURIComponent(keyword)}`;
        } else {
            alert("검색어를 입력하세요.");
        }
    });

});
