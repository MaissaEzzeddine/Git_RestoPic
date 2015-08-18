<?php
$servername = "mysql.hostinger.fr";
$username = "u129611711_root";
$password = "loulou1loulou";
$dbname = "u129611711_data";

$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
     die("Connection failed: " . $conn->connect_error);
} 

$url =$_POST[('url')];
$sql = "SELECT id,user_id FROM photos WHERE url LIKE '$url' ";
$result = $conn->query($sql);

if ($result->num_rows == 1) {
     while($row = $result->fetch_assoc()) {
	$id_photo = $row["id"] ; 
	$id_user = $row["user_id"] ; 
    }
}
$sql2 = "SELECT id,date_activation,date_expiration  FROM coupons WHERE photo_id = $id_photo";
$resul = $conn->query($sql2);
	if ($resul->num_rows == 1) {
              while($row = $resul->fetch_assoc()) {
                $response["id_coupon"] = $row["id"];
		$response["id_photo"] =  $id_photo;
		$response["id_user"] = $id_user;
		 $response["date_activation"] = $row["date_activation"];
		 $response["date_expiration"] = $row["date_expiration"];

     }
    $response["error"] = false;
    echo json_encode($response);
} else {
    $response["error"] = true;
    echo json_encode($response);
}
$conn->close();
?>