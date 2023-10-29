function createInitMap(mapContainer) {
    var myMap = new ymaps.Map(mapContainer, {
        center: [55.76, 37.64],
        zoom: 10
    }, {
        searchControlProvider: 'yandex#search'
    });

    return myMap;
}

function createPlaceMap(mapContainer, place) {
    var myMap = new ymaps.Map(mapContainer, {
        center: [place.lat, place.lng],
        zoom: 10
    }, {
        searchControlProvider: 'yandex#search'
    });

    myMap.geoObjects
        .add(new ymaps.Placemark([place.lat, place.lng], {
            balloonContent:
                '<p style="text-align: center;"><strong>' + place.name + '</strong></p>' +
                '<p style="text-align: center; color: #5c5c5c;">' + place.address + '</p>'
        }, {
            preset: 'islands#dotIcon',
            iconColor: '#023df7'
        }));

    return myMap;
}

function addPlacesNearby(myMap, places_nearby) {
    places_nearby.forEach(function (place_nearby, index) {
        var balloon_content =
            '<p style="text-align: center;"><strong>' + place_nearby.name + '</strong></p>' +
            '<p style="text-align: center; color: #5c5c5c;">' + place_nearby.address + '</p>';
        if (place_nearby.imageUrl != null) {
            balloon_content = balloon_content + '<p style="text-align: center;"><img src="' + place_nearby.imageUrl + '" width="70%"></p>';
        }
        if (place_nearby.description != null) {
            balloon_content = balloon_content + '<p>' + place_nearby.description + '</p>';
        }

        myMap.geoObjects
            .add(new ymaps.Placemark([place_nearby.lat, place_nearby.lng], {
                balloonContent: balloon_content
            }, {
                preset: 'islands#dotIcon',
                iconColor: '#12b918'
            }))
    });
}