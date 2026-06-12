const sceneList = document.querySelector('.scene-list');
const sceneContent = document.querySelector('.scene-content');
const sceneTableHead = document.querySelector('.scene-table-head');
const movieSearch = 'searchMovieList.json'; //가져올 내용
const openStartDt = '2026' // 개봉년도

let currentPage=1;
const pageSize=10;
let currentMode="movieList";
const pageBlockSize = 5;


const sceneListFn = (page = 1) => {
    currentMode = "movieList";
    currentPage = page;

    const apiURL = `/open/scene/movieList?page=${page}&size=${pageSize}`;
    fetch(apiURL)
        .then(res => res.json())
        .then(rs => {
            console.log(rs);
            let movieListResult;
            try {
                const movie = rs.movie; // 문자열 파싱
                movieListResult = movie;
            } catch (e) {
                console.error("JSON 파싱 오류:", e);
                alert("JSON 파싱 오류")
                return;
            }
            if (!movieListResult || !Array.isArray(movieListResult)) {
                console.error("movieList가 존재하지 않음", movieListResult);
                alert("movieList가 존재하지 않음")
                return;
            }
            let sceneHead = '';
            sceneHead += `
            <tr>
                    <th>ID</th>
                    <th>제목</th>
                    <th>개봉일</th>
                    <th>장르</th>
                    <th>국적</th>
                    <th>상태</th>
                    <th>영화코드</th>
                    <th>보기</th>
                </tr>`

            let html1 = '';
            movieListResult.forEach(el => {
                html1 += `
                    <tr>
                        <td>${el.movieCd}</td>
                        <td>${el.movieNm}</td>
                        <td>${el.openDt}</td>
                        <td>${el.genreAlt}</td>
                        <td>${el.repNationNm}</td>
                        <td>${el.prdtStatNm}</td>
                        <td>${el.movieCd}</td>
                        <td><button onClick="sceneDetailFn(${el.movieCd})">보기</button></td>
                    </tr>
                `;
            })
            html1 += `
            <tr><td colspan="7" style="text-align: center">출처: ${rs.movie[0].source}, 총편수: ${rs.movie[0].totCnt}</td> </tr> `;
            sceneTableHead.innerHTML = sceneHead;
            sceneContent.innerHTML = html1;
            renderPagination(page, rs.totalPages || 1);
        }).catch(err => console.log(err));
}

//영화 상세조회
const sceneDetailFn = (movieCd) => {
//    const apiURL = `/rest/movieDetail/${movieCd}`;
    const apiURL = `/open/scene/movieDetailJava/${movieCd}`;
//
    fetch(apiURL)
        .then(res => res.json())
        .then(rs => {
            // console.log(rs);  console.log(rs.movie);

            document.querySelector('.scene-detail-modal').classList.add('show');
            // API 응답에서 문자열로 받은 JSON을 파싱
            const movie = JSON.parse(rs.movie);
            // const movieStr = rs.movie;
            console.log(movie);
            // movieInfoResult 추출
            const movieInfoResult = movie.movieInfoResult;
            const movieInfo = movieInfoResult.movieInfo;
            // 영화 정보 추출
            const movieId = movieInfo.id;  //삭제시
            const movieNm =  movieInfo.movieNm;
            const movieCdVal = movieInfo.movieCd;
            const openDt = movieInfo.openDt;
            const prdtStatNm = movieInfo.prdtStatNm;
            const nationNm = movieInfo.nations?.map(n => n.nationNm).join(", ") || "-";
            const genreAlt = movieInfo.genres?.map(g => g.genreNm).join(", ") || "-";
            const typeNm = movieInfo.typeNm;
            const movieNmEn = movieInfo.movieNmEn;
            const source = movieInfo.source
            const watchGradeNm = movieInfo.audits?.[0]?.watchGradeNm || "-";
            console.log(movieInfo.movieNm);
            const html1 = `
                <span class="modal-close-btn" onclick="closeModal()">X</span>
                <h1>${movieNm} (${movieCdVal})</h1>
                <ul>
                    <li>개봉일: ${openDt}</li>
                    <li>상태: ${prdtStatNm}</li>
                    <li>국적: ${nationNm}</li>
                    <li>등급: ${watchGradeNm}</li>
                    <li>장르: ${genreAlt}</li>
                    <li>영문: ${movieNmEn}</li>
                </ul>
            `;

            document.querySelector('.scene-detail-modal-con').innerHTML = html1;
        })
        .catch(err => alert("영화 상세 정보 로딩 중 오류 발생: " + err.message));
};

const closeModal = () => {
    document.querySelector('.scene-detail-modal').classList.remove('show');
}


const boxOfficeFn = () => {
    let apiURL = "";
    const dateInputEl = document.getElementById("targetDt");
    const weekGbEl = document.querySelector("input[name='weekGb']:checked");
    // 오늘 날짜 구하기
    const getToday = () => {
        const today = new Date();

        today.setDate(today.getDate() - 7);

        const yyyy = today.getFullYear(); // 년도 2025
        const mm = String(today.getMonth() + 1).padStart(2, '0'); // 9 -> 09
        const dd = String(today.getDate()).padStart(2, '0'); // 9 -> 09
        return `${yyyy}${mm}${dd}`;
    };
    // 요소가 없을 경우 기본값으로 요청
    if (!dateInputEl || !weekGbEl) {
        apiURL = `/open/scene/boxOffice/${getToday()}/0`; // 오늘 날짜, 주간(0) 기본
    } else {
        const dateInputVal = dateInputEl.value;
        const weekGb = weekGbEl.value;
        if (!dateInputVal) {
            alert("날짜를 선택해주세요.");
            return;
        }
        const targetDt = dateInputVal.replaceAll("-", "");
        apiURL = `/open/scene/boxOffice/${targetDt}/${weekGb}`;
    }
    // 나머지 fetch 코드는 동일하게 유지
    fetch(apiURL)
        .then(res => res.json())
        .then(rs => {
            const boxOfficeResult = rs.boxOfficeResult;
            if (!boxOfficeResult || boxOfficeResult.length === 0) {
                alert("조회된 박스오피스 데이터가 없습니다.");
                return;
            }
            let html2 = "";
            boxOfficeResult.forEach(el => {
                html2 += `
 <tr>
 <td>${el.rank}</td>
 <td>${el.movieNm}</td>
 <td>${el.openDt}</td>
 <td>${Number(el.audiAcc).toLocaleString()}</td>
 <td>${Number(el.salesAcc).toLocaleString()}</td>
 <td>${el.movieCd}</td>
 <td><button onclick="boxOfficeDetailFn('${el.movieCd}')">보기</button></td>
 </tr>
 `;
            });
            let sceneHead = '';
            sceneHead += `
 <tr>
 <td colspan="7" >
 <div class="box-office-form" >
 <label for="targetDt"> 날짜 선택 (YYYY-MM-DD):</label>
 <input type="date" id="targetDt">
<label "> 주간 구분:</label>
 <label><input type="radio" name="weekGb" value="0" checked> 주간</label>
 <label><input type="radio" name="weekGb" value="1"> 주말</label>
 <label><input type="radio" name="weekGb" value="2"> 평일</label>
 <button onclick="boxOfficeFn()" >조회</button>
 </div>
 </td>
 </tr>
 <tr>
 <td colspan="7" >
 <span>${boxOfficeResult[0].boxofficeType}</span> · <span>${boxOfficeResult[0].showRange}</span>
 </td>
 </tr>
 <tr>
 <th>순위</th>
 <th>제목</th>
 <th>개봉일</th>
 <th>누적관객수</th>
 <th>누적매출액</th>
 <th>영화코드</th>
 <th>보기</th>
 </tr>
 `;
            sceneTableHead.innerHTML = sceneHead;
            sceneContent.innerHTML = html2;
            clearPagination();
        })
        .catch(err => {
            console.error("API 호출 중 에러:", err);
            alert("박스오피스 데이터를 불러오지 못했습니다.");
        });
};
const boxOfficeDetailFn = (movieCd) => {
// const apiURL = `/rest/movieDetail/${movieCd}`;
    const apiURL = `/open/scene/boxOfficeDetailJava/${movieCd}`;
//
    fetch(apiURL)
        .then(res => res.json())
        .then(rs => {
            console.log(rs);
            console.log(rs.movie);
            document.querySelector('.scene-detail-modal').classList.add('show');
            const movieStr = rs.movie;
            const movie = movieStr;
            const movieInfoResult = movie;
            const movieInfo = movieInfoResult;
            // 영화 정보 추출
            const movieId = movieInfo.id; //삭제시
            const movieNm = movieInfo.movieNm;
            const movieCdVal = movieInfo.movieCd;
            const openDt = movieInfo.openDt;
            const rank = movieInfo.rank;
            const audiAcc = movieInfo.audiAcc;
            const salesAcc = movieInfo.salesAcc;
            const html1 = `
 <span class="modal-close-btn" onclick="closeModal()">X</span>
 <h1>${movieNm} (${movieCdVal})</h1>
 <ul>
 <li>개봉일: ${openDt}</li>
 <li>영화명: ${movieNm}</li>
 <li>랭킹: ${rank}</li>
 <li>누적관객수: ${audiAcc}</li>
 <li>누적매출액: ${salesAcc}</li>
 <li><button>삭제</button></li>
 </ul>
 `;
            document.querySelector('.scene-detail-modal-con').innerHTML = html1;
        })
        .catch(err => alert("영화 상세 정보 로딩 중 오류 발생: " + err.message));
};
document.querySelectorAll('.menu-left ul li')
    .forEach(li => {
        li.addEventListener('click', () => {

            document.querySelectorAll('.menu-left ul li')
                .forEach(item => item.classList.remove('active'));

            li.classList.add('active');
        });
    });

//페이징 함수
const renderPagination = (page, totalPages) => {
    const pagination = document.querySelector('.pagination');
    if (!pagination) return;
    const startPage =
        Math.floor((page - 1) / pageBlockSize)
        * pageBlockSize + 1;

    const endPage =
        Math.min(
            startPage + pageBlockSize - 1,
            totalPages
        );
    let html = `<ul>`;

    html += `
        <li>
            <button ${page <= 1 ? 'disabled' : ''} onclick="changePage(1)">처음</button>
        </li>
    `;

    html += `
        <li>
            <button ${page <= 1 ? 'disabled' : ''} onclick="changePage(${page - 1})">이전</button>
        </li>
    `;
    for(let i=startPage; i<=endPage; i++) {
        html += `
            <li>
                <button class="${page === i ? 'active' : ''}" onclick="changePage(${i})">
                    ${i}
                </button>
            </li>
        `;
    }
    html += `
        <li>
            <button ${page >= totalPages ? 'disabled' : ''} onclick="changePage(${page + 1})">다음</button>
        </li>
    `;
    html += `
        <li>
            <button ${page >= totalPages ? 'disabled' : ''} onclick="changePage(${totalPages})">마지막</button>
        </li>
    `;

    html += `</ul>`;
    pagination.innerHTML = html;
};
const changePage = (page) => {
    if (currentMode === "movieList") {
        sceneListFn(page);
    }

    if (currentMode === "boxOffice") {
        boxOfficeFn(null, page);
    }
};
const clearPagination = () => {
    const pagination = document.querySelector('.pagination');

    if (pagination) {
        pagination.innerHTML = '';
    }
}

(() => {
    sceneListFn();
})();