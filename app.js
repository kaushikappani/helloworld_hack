require('dotenv').config();
const fs = require('fs');
const ejs = require('ejs');
const express = require('express');
const bodyParser = require('body-parser');


const app = express();

const axios = require('axios');




app.use(bodyParser.urlencoded({
    extended: true
}))

app.use(express.static(__dirname + '/public'));


app.set("view engine", "ejs");




app.post('/predictor', (req, res) => {
    axios.post('https://us-central1-imperial-welder-273309.cloudfunctions.net/get_admittance', {
        "GRE Score": req.body.gre,
        "TOEFL Score": req.body.toefl,
        "uni": req.body.uni,
        "SOP": req.body.sop,
        "LOR": req.body.lor,
        "CGPA": req.body.cgpa,
        "Research": req.body.res,
        "sports": req.body.spo,
        "certifications": req.body.cert
    }).then((data) => {
        res.redirect('/predictor?chance=' + data.data);
    })
})
app.get('/', (req, res) => {
    res.render('index');
});

app.get('/predictor', (req, res) => {
    if (req.query.chance) {
        var chance = req.query.chance
    } else {
        var chance = null;
    }
    res.render('predictor', {
        chance:chance
    })
})

app.listen(3000, (req, res) => {
    console.log('123')
})