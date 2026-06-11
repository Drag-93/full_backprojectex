const search = document.querySelector("#weatherSearch");

const description = document.querySelector(".city .description");
const icon = document.querySelector(".city .icon");
const cityCon = document.querySelector(".city .con");
const tempMinCon = document.querySelector(".temp_min .con");
const tempMaxCon = document.querySelector(".temp_max .con");
const sunriseCon = document.querySelector(".sunrise .con");
const sunsetCon = document.querySelector(".sunset .con");

let map = null;
let marker = null;

window.weatherSearchFn = () => {
    const appURL = `/open/weather/search/${search.value}`;

    fetch(appURL)
        .then(res => res.json())
        .then(rs => {
            console.log(rs);

            const weather = JSON.parse(rs.weather);

            cityCon.innerText = weather.name;
            description.innerText = weather.weather[0].description;

            icon.innerHTML = `
                <img src="https://openweathermap.org/img/wn/${weather.weather[0].icon}@2x.png" alt="weather icon">
            `;

            tempMaxCon.innerText = `${(weather.main.temp_max - 273.15).toFixed(1)}℃`;
            tempMinCon.innerText = `${(weather.main.temp_min - 273.15).toFixed(1)}℃`;

            sunriseCon.innerText = new Date(weather.sys.sunrise * 1000).toLocaleTimeString();
            sunsetCon.innerText = new Date(weather.sys.sunset * 1000).toLocaleTimeString();

            weatherMapFn(weather.coord.lat, weather.coord.lon);
        })
        .catch(err => {
            console.log(err);
            alert("날씨 정보를 불러오지 못했습니다.");
        });
};

function weatherMapFn(lat, lon) {
    const mapContainer = document.getElementById("map");
    const position = new kakao.maps.LatLng(lat, lon);

    if (!map) {
        map = new kakao.maps.Map(mapContainer, {
            center: position,
            level: 5
        });

        marker = new kakao.maps.Marker({
            position: position
        });

        marker.setMap(map);
    } else {
        map.setCenter(position);
        marker.setPosition(position);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    weatherSearchFn();
});