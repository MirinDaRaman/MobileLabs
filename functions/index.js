const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
const json =[
    {
        "title":"Terminator 2",
        "year":1992,
        "rating":5.0,
        "description":"A cyborg, identical to the one who failed to kill Sarah Connor, ",
        "poster":"https://images-na.ssl-images-amazon.com/images/I/81EiA0A8UkL._SY606_.jpg"
    },
    {
        "title":"Titanic",
        "year":1997,
        "rating":4.5,
        "description":"A seventeen-year-old aristocrat falls in love with a kind but poor artist aboard the ",
        "poster":"https://images-na.ssl-images-amazon.com/images/I/717KH%2Bf1fkL._SY550_.jpg"
    },
    {
        "title":"Avatar",
        "year":2009,
        "rating":4.0,
        "description":"A paraplegic marine dispatched to the moon Pandora on a unique mission becomes torn between ",
        "poster":"https://images-na.ssl-images-amazon.com/images/I/61OUGpUfAyL._SY679_.jpg"
    },
    {
        "title":"True lies",
        "year":1994,
        "rating":3.5,
        "description":"A fearless, globe-trotting, terrorist-battling secret agent has his life turned upside down ",
        "poster":"https://images-na.ssl-images-amazon.com/images/I/818bm0-VD3L._SY500_.jpg"
    }
]; exports.custom_movies = functions.https.onRequest((request,response) =>{
    response.send(json);
});
