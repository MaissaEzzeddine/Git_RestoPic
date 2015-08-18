<?php
 
class PassHash {
     private static $algo = '$2a';
    private static $cost = '$10';
     public static function unique_salt() {
        return substr(sha1(mt_rand()), 0, 22);
    }
     public static function hash($motdepasse) {
        return crypt($motdepasse, self::$algo .
                self::$cost .
                '$' . self::unique_salt());
    }
     public static function check_password($hash, $motdepasse) {
        $full_salt = substr($hash, 0, 29);
        $new_hash = crypt($motdepasse, $full_salt);
        return ($hash == $new_hash);
    }
}
?>