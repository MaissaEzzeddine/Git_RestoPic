<?php
$servername = "mysql.hostinger.fr";
$username = "u129611711_root";
$password = "loulou1loulou";
$dbname = "u129611711_data";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
     die("Connection failed: " . $conn->connect_error);
} 

 $user_id =$_POST[('user_id')];

$sql = "SELECT url,date_de_prise FROM photos WHERE user_id=$user_id";
$result = $conn->query($sql);

$response["pictures"] = array();

if ($result->num_rows > 0) {
     // output data of each row
     while($row = $result->fetch_assoc()) {
        $picture = array();
        $picture["url"] = $row["url"];
        $picture["date_de_prise"] = $row["date_de_prise"];
        // push single product into final response array
        array_push($response["pictures"], $picture);
     }
    $response["error"] = false;
    echo json_encode($response);
} else {
    $response["error"] = true;
    $response["message"] = "No pictures found";
     echo json_encode($response);
}

$conn->close();
?>