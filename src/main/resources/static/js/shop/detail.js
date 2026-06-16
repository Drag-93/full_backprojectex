const itemPriceEl = document.getElementById("itemPrice");
const itemSizeEl = document.getElementById("itemSize");
const totalEl = document.getElementById("total");

function totalPriceFn() {
    const price = Number(itemPriceEl.value);
    const count = Number(itemSizeEl.value);

    totalEl.innerText = (price * count).toLocaleString() + "원";
}

// 상품개수 변경 시 실행
itemSizeEl.addEventListener("input", totalPriceFn);

// 페이지 최초 로딩 시 실행
totalPriceFn();