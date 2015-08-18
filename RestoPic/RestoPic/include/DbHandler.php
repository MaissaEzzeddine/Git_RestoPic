<?php
class DbHandler {
    private $conn;
    function __construct() {
        require_once 'DbConnect.php';
        $db = new DbConnect();
        $this->conn = $db->connect();
    }
    public function createUser($nom,$prenom,$email,$motdepasse) {
        require_once 'PassHash.php';
        $response = array();
        if (!$this->isUserExists($email)) {
            $password_hash = PassHash::hash($motdepasse);
            $stmt = $this->conn->prepare("INSERT INTO utilisateurs(nom,prenom,email,motdepasse) values(?,?,?,?)");
            $stmt->bind_param("ssss",$nom,$prenom,$email,$password_hash);
            $result = $stmt->execute();
            $stmt->close();
             if ($result) {
                return USER_CREATED_SUCCESSFULLY;
            } else {
                return USER_CREATE_FAILED;
            }
        } else {
            return USER_ALREADY_EXISTED;
        } 
        return $response;
    }
    
    public function createUserFacebook($nom,$prenom,$email) {
        $response = array();
        if (!$this->isUserExists($email)) {
            $stmt = $this->conn->prepare("INSERT INTO utilisateurs(nom,prenom,email) values(?,?,?)");
            $stmt->bind_param("sss",$nom,$prenom,$email);
            $result = $stmt->execute();
	    $pid=mysqli_insert_id($this->conn);
            $stmt->close();
             if ($result) {
                return $pid;
            } else {
                return USER_CREATE_FAILED;
            }
        } else {
            return USER_ALREADY_EXISTED;
        } 
        return $response;
    }
    public function checkLogin($email, $passe) {
        $stmt = $this->conn->prepare("SELECT motdepasse FROM utilisateurs WHERE email = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $stmt->bind_result($motdepasse);
        $stmt->store_result();
        if ($stmt->num_rows > 0) {
            $stmt->fetch();
            $stmt->close();
            if (PassHash::check_password($motdepasse, $passe)) {
                return TRUE;
            } else {
                return FALSE;
            }
        } else {
            $stmt->close();
             return FALSE;
        }
    }
    public function isUserExists($email) {
        $stmt = $this->conn->prepare("SELECT id from utilisateurs WHERE email = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }
    public function getUserByEmail($email) {
        $stmt = $this->conn->prepare("SELECT id,nom,prenom,email FROM utilisateurs WHERE email = ?");
        $stmt->bind_param("s", $email);
        if ($stmt->execute()) {
        $stmt->bind_result($id ,$nom , $prenom, $email);

        /* fetch values */
        mysqli_stmt_fetch($stmt);

        /* set values */
        $user['id'] = $id;
        $user['nom'] = $nom;
        $user['prenom'] = $prenom;
        $user['email'] = $email;
            
           $stmt->close();
            return $user;
        } else {
            return NULL;
        }
    }

public function getUserFacebook($email) {
        $stmt = $this->conn->prepare("SELECT id FROM utilisateurs WHERE email = ?");
        $stmt->bind_param("s", $email);
        if ($stmt->execute()) {
        $stmt->bind_result($id);
        mysqli_stmt_fetch($stmt);            
           $stmt->close();
            return $id;
        } else {
            return NULL;
        }
    }
    
public function random_int()
{
    $int_set_array = array();
    $int_set_array[] = array('count' => 4, 'chiffres' => '0123456789');
    $temp_array = array();
    foreach ($int_set_array as $int_set) {
        for ($i = 0; $i < $int_set['count']; $i++) {
            $temp_array[] = $int_set['chiffres'][rand(0, strlen($int_set['chiffres']) - 1)];
        }
    }
    shuffle($temp_array);
    return implode('', $temp_array);
}

 
public function forgotPassword($email, $code){
    
    $stmt = $this->conn->prepare("UPDATE utilisateurs u SET u.code = ? WHERE u.email = ?");
    $stmt->bind_param("ss",$code,$email);
    $stmt->execute();
    $num_affected_rows = $stmt->affected_rows;
    $stmt->close();
    return $num_affected_rows > 0;    
}

public function codeenv($email,$code){
    $stmt = $this->conn->prepare("SELECT code FROM utilisateurs WHERE email = ?");
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $stmt->bind_result($codebd);
    $stmt->store_result();
        if ($stmt->num_rows > 0) {
            $stmt->fetch();
            $stmt->close();
            if ( $code == $codebd) {
                return TRUE;
            } else {
                return FALSE;
            }
        } else {
            $stmt->close();
             return FALSE;
        }   
}


public function changepass($email,$pass){
    require_once 'PassHash.php';
    $password_hash = PassHash::hash($pass);
    $stmt = $this->conn->prepare("UPDATE utilisateurs u SET u.motdepasse = ? WHERE u.email = ?");
    $stmt->bind_param("ss",$password_hash,$email);
    $stmt->execute();
    $num_affected_rows = $stmt->affected_rows;
    $stmt->close();
    return $num_affected_rows > 0;    
}

public function registerpicture($url,$user_id,$date_de_prise) {
            $response = array();
            $stmt = $this->conn->prepare("INSERT INTO photos(url,user_id,date_de_prise) values('{$url}','{$user_id}','{$date_de_prise}')");
            $result = $stmt->execute();
            $pid=mysqli_insert_id($this->conn);
            $stmt->close();
             if (  $result  ) {
		return $pid ;
            } else {
                 return FALSE;
            }
                 return $response;
    }

public function affectercoupon($id,$date_activation) {
            $response = array();
            $date_a = new DateTime($date_activation);
            $date_e = new DateTime($date_activation);
            date_add($date_e,date_interval_create_from_date_string("7 days"));
            $date_expiration = $date_e->format('Y-m-d H:i:s'); 
            $stmt = $this->conn->prepare("INSERT INTO coupons (photo_id,date_activation, date_expiration) values(?,?,?)");
            $stmt->bind_param("iss",$id,$date_activation,$date_expiration);
            $result = $stmt->execute();
            $stmt->close();
             if (  $result  ) {
		return TRUE ;
            } else {
                 return FALSE;
            }
                 return $response;
    }
}