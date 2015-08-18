<?php

 require "../include/DbHandler.php";
 $db = new DbHandler();

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

$user_id = filter_input(INPUT_POST, 'user_id', FILTER_VALIDATE_INT);

$date_de_prise = mysqli_real_escape_string($conn,$_POST['date_de_prise']);
$date_de_prise = preg_replace('#(\d{2})/(\d{2})/(\d{4})\s(.*)#', '$3-$2-$1 $4', $date_de_prise);

$file = basename( $_FILES['uploaded_file']['name']);
$url ='http://restopic.esy.es/RestoPic/pictures/'.basename( $_FILES['uploaded_file']['name']);
$response= array() ;
$res = $db->registerpicture($url,$user_id,$date_de_prise);
            if ($res !=FALSE ) {
                if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file))
              {
		$coupon=$db->affectercoupon($res,$date_de_prise);
		if ( $coupon == TRUE ) 
		{
                $response["error"] = false;
                $response["message"] = "You got coupon";
                echo json_encode($response);
		}
		else if ($res ==FALSE) {
                $response["error"] = true;
                $response["message"] = "Oops! no coupon";
                echo json_encode($response);
               }
             }
                else  {
                $response["error"] = true;
                $response["message"] = "Oops! no uploas";
                echo json_encode($response);
            }
            } else if ($res ==FALSE) {
                $response["error"] = true;
                $response["message"] = "Oops! no database";
                echo json_encode($response);
            }
?>