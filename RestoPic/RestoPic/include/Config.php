<?php
/**
 * Database configuration
 */

$mysql_host = "mysql.hostinger.fr";
$mysql_database = "u129611711_data";
$mysql_user = "u129611711_root";
$mysql_password = "loulou1loulou";       

define('DB_USERNAME', $mysql_user);
define('DB_PASSWORD', $mysql_password);
define('DB_HOST', $mysql_host);
define('DB_NAME', $mysql_database);
 
define('USER_CREATED_SUCCESSFULLY', 0);
define('USER_CREATE_FAILED', 1);
define('USER_ALREADY_EXISTED', 2);
?>