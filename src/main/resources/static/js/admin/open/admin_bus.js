const search = document.querySelector('#search');
const bus = document.querySelector('#bus');
const busStationCon = document.querySelector('.bus-station-con');

// 🔍 버스 노선 검색
const busSearch = () => {
    const searchVal = search.value.trim();
    if (!searchVal) return alert("노선 번호를 입력하세요.");

    const apiUrl = `/open/bus/busList?searchVal=${searchVal}`;

    fetch(apiUrl)
        .then(res => res.json())
        .then(data => {
            console.log(data);
            console.log(data.busList);

            const itemList = data.busList;
            if (!itemList || itemList.length === 0) {
                bus.innerHTML = "<tr><td colspan='9'>검색 결과가 없습니다.</td></tr>";
                return;
            }

            let html1=``;
            itemList.forEach(el => {
                    html1+=`
              <tr>
                <td>${el.busRouteNm}</td>
                <td>${el.routeType}</td>
                <td>${el.stStationNm}</td>
                <td>${el.edStationNm}</td>
                <td>${el.firstBusTm}</td>
                <td>${el.lastBusTm}</td>
                <td>${el.term}</td>
               <td onclick="stationListFn('${el.busRouteId}')">정류장정보</td>
                <td>${el.corpNm}</td>
              </tr>
            `
                }
            );
            bus.innerHTML = html1;
        })
        .catch(err => console.error("버스 노선 검색 에러:", err));
};

// 🚌 노선 ID로 정류장 목록 조회
const stationListFn = (busRouteId) => {
    const apiUrl=`/open/bus/busStationList?busRouteId=${busRouteId}`;

    fetch(apiUrl)
        .then(res => res.json())
        .then(data => {
            console.log(data);
            console.log(data.busStationList);
            const itemList = data.busStationList;

            if (!itemList || itemList.length === 0) {
                busStationCon.innerHTML = "<p>정류장 정보가 없습니다.</p>";
                return;
            }

            let html1=`<ul>`;
            itemList.forEach(el=>{
                console.log(el.stationNm+", "+el.gpsX+" , "+el.gpsY)

                html1+=`
               <li style="cursor:pointer" onclick="stationOne('${el.stationNm}',${el.gpsX},${el.gpsY})">  ${el.stationNm}</li>
             `;
            });
            html1+=`</ul>`;
            busStationCon.innerHTML=html1
            // 카카오 지도에 정류장 마커 표시
            kakaoMapBusStationList(itemList);
        })
        .catch(err => console.error("정류장 목록 불러오기 에러:", err));
};

const busModal = document.querySelector('.bus-detail-modal');
const busModalCon = document.querySelector('.bus-detail-modal .bus-detail-modal-con');
//const busModalConH1 = document.querySelector('.bus-detail-modal ."bus-detail-modal-con h1');
const stationOne=(stNm,gX,gY)=>{
    console.log(stNm+", "+gX+", "+gY);
    busModal.classList.add('show');

    document.querySelector('.bus-detail-modal .bus-detail-modal-con h1').innerText=`${stNm}`;
//    const mapContainer = document.getElementsByClassName('map-view')[0], // 지도를 표시할 div
    const mapContainer = document.getElementById('bus-map-view'), // 지도를 표시할 div
        mapOption = {
            center: new kakao.maps.LatLng(gY, gX), // 지도의 중심좌표
            level: 3 // 지도의 확대 레벨
        };
    const map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
    // 마커가 표시될 위치입니다
    const markerPosition  =  new kakao.maps.LatLng(gY, gX); // 지도의 중심좌표
    // 마커를 생성합니다
    const marker = new kakao.maps.Marker({
        position: markerPosition
    });
    // 마커가 지도 위에 표시되도록 설정합니다
    marker.setMap(map);

    // 아래 코드는 지도 위의 마커를 제거하는 코드입니다
    // marker.setMap(null
}
const closeModal=(e)=>{
    busModal.classList.remove('show');
}
// 🗺️ 카카오 지도에 정류장 위치 표시
const kakaoMapBusStationList = (itemList) => {

    const positions = itemList.map(el => ({
        title: el.stationNm,
        latlng: new kakao.maps.LatLng(parseFloat(el.gpsY), parseFloat(el.gpsX))
    }));

    const mapContainer = document.getElementById('bus-map');
    const mapOption = {
        center: positions[0].latlng,
        level: 5
    };

    const map = new kakao.maps.Map(mapContainer, mapOption);

    const imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";
    const imageSize = new kakao.maps.Size(24, 35);
    const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

    positions.forEach(pos => {
        new kakao.maps.Marker({
            map,
            position: pos.latlng,
            title: pos.title,
            image: markerImage
        });
    });
};

// 페이지 로드 시 기본 검색 실행 (필요 없으면 제거 가능)
document.addEventListener("DOMContentLoaded", () => {
    busSearch();
});