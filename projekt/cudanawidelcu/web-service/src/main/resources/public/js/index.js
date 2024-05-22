const isUserAdmin = () => {
    const token = getCookie("jwtToken");
    if (!token) {
        return false; // Brak tokena w ciasteczkach
    }

    // Token JWT składa się z trzech części, oddzielonych kropkami
    const payloadBase64 = token.split('.')[1];
    if (!payloadBase64) {
        return false; // Nieprawidłowy format tokena
    }

    // Dekodowanie Base64
    const payloadJson = atob(payloadBase64);
    let payload;
    try {
        payload = JSON.parse(payloadJson);
    } catch (e) {
        return false; // Błąd podczas parsowania JSON
    }

    // Sprawdzanie atrybutu "ROLE"
    return payload.role === "ADMIN";
}