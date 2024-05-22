const express = require("express");
const multer = require("multer");
const path = require("path");
const fs = require("fs");
const cors = require("cors");  
const Eureka = require("eureka-js-client").Eureka;

const app = express();
const port = 3000;

const uploadDirectory = "C:\\Users\\funko\\Documents\\img";
app.use(cors());  

// Tworzenie folderu jeśli nie istnieje
if (!fs.existsSync(uploadDirectory)) {
  console.log("FOLDER NIE ISTNIEJE");
}

// Konfiguracja multera do zapisywania plików
const storage = multer.diskStorage({
  destination: function (req, file, cb) {
    cb(null, uploadDirectory);
  },
  filename: function (req, file, cb) {
    cb(null, file.originalname); // Zapisz plik z oryginalną nazwą
  },
});

const upload = multer({ storage: storage });

// Endpoint do zapisywania plików
app.post("/upload", upload.single("photo"), (req, res) => {
  console.log("UPLOAD");
  res.send("Plik został pomyślnie przesłany");
});

// Endpoint do pobierania plików
app.get("/images/:filename", (req, res) => {
  console.log("GET");
  const filename = req.params.filename;
  const filePath = path.join(uploadDirectory, filename);

  // Sprawdzenie czy plik istnieje
  if (fs.existsSync(filePath)) {
    // Odczytanie pliku i przesłanie w odpowiedzi
    res.sendFile(filePath);
  } else {
    res.status(404).send("Nie znaleziono pliku");
  }
});

app.get("/", (req, res) => {
  console.log("HOME");
  res.send("home");
});

app.get("/info", (req, res) => {
  res.json({
    status: "UP",
    info: "Images service is running",
  });
});

const client = new Eureka({
  instance: {
    app: "images-service",
    hostName: "localhost",
    ipAddr: "127.0.0.1",
    statusPageUrl: "http://localhost:3000/info",
    port: {
      $: 3000,
      "@enabled": "true",
    },
    vipAddress: "localhost",
    dataCenterInfo: {
      "@class": "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo",
      name: "MyOwn",
    },
  },
  eureka: {
    host: "localhost",
    port: 1111,
    servicePath: "/eureka/apps/",
  },
});

app.listen(port, () => {
  console.log(`Serwer uruchomiony na http://localhost:${port}`);

  client.start((error) => {
    if (error) {
      console.log("Błąd podczas rejestracji w Eureka:", error);
    } else {
      console.log("Pomyślnie zarejestrowano w Eureka");
    }
  });
});

process.on("SIGINT", () => {
  client.stop((error) => {
    if (error) {
      console.log("Błąd podczas wyrejestrowania z Eureka:", error);
    } else {
      console.log("Pomyślnie wyrejestrowano z Eureka");
    }
    process.exit();
  });
});
