<?php
require "../include/DbHandler.php";
require "../include/PassHash.php"; 
require "../libs/Slim/Slim.php";
require "PHPMailerAutoload.php";
require "class.phpmailer.php";
require "class.smtp.php";



\Slim\Slim::registerAutoloader();
$app = new \Slim\Slim();
$user_id = NULL; 

function verifyRequiredParams($required_fields) {
    $error = false;
    $error_fields = "";
    $request_params = array();
    $request_params = $_REQUEST;
    if ($_SERVER['REQUEST_METHOD'] == 'PUT') {
        $app = \Slim\Slim::getInstance();
        parse_str($app->request()->getBody(), $request_params);
    }
    foreach ($required_fields as $field) {
        if (!isset($request_params[$field]) || strlen(trim($request_params[$field])) <= 0) {
            $error = true;
            $error_fields .= $field . ', ';
        }
    }
     if ($error) {
        $response = array();
        $app = \Slim\Slim::getInstance();
        $response["error"] = true;
        $response["message"] = 'Required field(s) ' . substr($error_fields, 0, -2) . ' is missing or empty';
        echoRespnse(400, $response);
        $app->stop();
    }
}
function validateEmail($email) {
    $app = \Slim\Slim::getInstance();
    if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $response["error"] = true;
        $response["message"] = 'Email address is not valid';
        echoRespnse(400, $response);
        $app->stop();
    }
}
function echoRespnse($status_code, $response) {
    $app = \Slim\Slim::getInstance();
    // Http response code
    $app->status($status_code);
 
    // setting response content type to json
    $app->contentType('application/json');
 
    echo json_encode($response);
}
$app->post('/register', function() use ($app) {verifyRequiredParams(array('nom','prenom', 'email', 'motdepasse'));
            $response = array();
             $nom = $app->request->post('nom');
             $prenom = $app->request->post('prenom');
             $email = $app->request->post('email');
             $motdepasse = $app->request->post('motdepasse');
             validateEmail($email);
            $db = new DbHandler();
            $res = $db->createUser($nom,$prenom, $email, $motdepasse);
            if ($res == USER_CREATED_SUCCESSFULLY) {
                $response["error"] = false;
                $response["message"] = "You are successfully registered";
                echoRespnse(201, $response);
            } else if ($res == USER_CREATE_FAILED) {
                $response["error"] = true;
                $response["message"] = "Oops! An error occurred while registereing";
                echoRespnse(200, $response);
            } else if ($res == USER_ALREADY_EXISTED) {
                $response["error"] = true;
                $response["message"] = "Sorry, this email already existed";
                echoRespnse(200, $response);
            }
        });       
$app->post('/register_facebook', function() use ($app) {verifyRequiredParams(array('nom','prenom', 'email'));
            $response = array();
             $nom = $app->request->post('nom');
             $prenom = $app->request->post('prenom');
             $email = $app->request->post('email');
             validateEmail($email);
            $db = new DbHandler();
            $res = $db->createUserFacebook($nom,$prenom,$email);
             if ($res == USER_CREATE_FAILED) {
                $response["error"] = true;
                $response["message"] = "Oops! An error occurred while registereing";
                echoRespnse(200, $response);
            } else if ($res == USER_ALREADY_EXISTED) {
		$result = $db->getUserFacebook($email);
                $response["error"] = false;
		$response["id"] = $result;
                $response["message"] = "existed";
                echoRespnse(200, $response);
            } else {
                $response["error"] = false;
                $response["message"] = "success";
		$response["id"] = $res;
                echoRespnse(201, $response);
            }
        });
$app->post('/login', function() use ($app) {
            // check for required params
            verifyRequiredParams(array('email', 'motdepasse'));
 
            // reading post params
            $email = $app->request()->post('email');
            $motdepasse = $app->request()->post('motdepasse');
            $response = array();
            $db = new DbHandler();
            // check for correct email and password
            if ($db->checkLogin($email, $motdepasse)) {
                // get the user by email
                $user = $db->getUserByEmail($email);
 
                if ($user != NULL) {
                    $response["error"] = false;
                    $response['id'] = $user['id'];
                    $response['nom'] = $user['nom'];
                    $response['prenom'] = $user['prenom'];
                    $response['email'] = $user['email'];
                } else {
                    // unknown error occurred
                    $response['error'] = true;
                    $response['message'] = "An error occurred. Please try again";
                }
            } else {
                // user credentials are wrong
                $response['error'] = true;
                $response['message'] = 'Login failed. Incorrect credentials';
            }
            echoRespnse(200, $response);
        });       
$app->post('/forgotpassword', function() use ($app) {
        
    $email = $app->request()->post('email');
    $response = array();
    $db = new DbHandler();
    $randomcode = $db->random_int();
    
  if ($db->isUserExists($email)) {
        $user = $db->forgotPassword($email, $randomcode);

    if ($user) {
    $response["error"] = false;
    $mail = new PHPMailer();
    
    $mail->Host     = "restopic.16mb.com"; //server
    $mail->Port = 465;                                    // TCP port to connect to
    $mail->Username = "maissa@restopic.16mb.com"; 
    $mail->Password = "loulou"; 
    $mail->From  = "maissa@restopic.16mb.com";
    $mail->Subject  = "Password Recovery";
    $mail->Body     = "Hello User,nnYour Password is sucessfully changed. Your new Password is $randomcode . Login with your new Password and change it in the User Panel.nnRegards,nLearn2Crack Team.";
    $mail->AddAddress($email);
    $mail->WordWrap = 50;
    if(!$mail->Send()) {
          $response["message"] = "mail non envoyé";
          $response["Mailer error:"] = $mail->ErrorInfo;

    } else {
          $response["message"] = "mail envoyé";
    }
   }
   
  }
    else {
        $response["error"] = true;
        $response["message"] = "User not exist";
     } 
echoRespnse(200, $response);
}); 
$app->post('/code', function() use ($app) {
        
    verifyRequiredParams(array('email','code'));
    $email = $app->request()->post('email');
    $code = $app->request()->post('code');
    $response = array(); 
    
    $db = new DbHandler();
            // check for correct email and password
            if ($db->codeenv($email, $code)) {
                // get the user by email
                
                    $response["error"] = false;
                    $response['message'] = 'code correct';

                    
                
            } else {
                // user credentials are wrong
                $response['error'] = true;
                $response['message'] = 'code incorrect';
            }
    
     
echoRespnse(200, $response);
}); 
$app->post('/newpassword', function() use ($app) {
        
    verifyRequiredParams(array('email','motdepasse'));
    $email = $app->request()->post('email');
    $password = $app->request()->post('motdepasse');
    $response = array(); 
    $db = new DbHandler();
            // check for correct email and password
            if ($db->changepass($email,$password)) {
                // get the user by email
                    $response["error"] = false;
                    $response['message'] = 'mot de passe changé';
       
            } else {
                // user credentials are wrong
                $response['error'] = true;
            }
echoRespnse(200, $response);
});  


$app->run();