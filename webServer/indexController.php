<?php

require_once 'sample.php';


$directory = Sample::$SAMPLES_DIR;
$list = scandir($directory);

$samples = [];

foreach ($list as $elem) {
    $directory_sample = $directory."/".$elem;
    
    if (($elem != ".") && ($elem != "..") && (is_dir($directory_sample))) {
        
        $sample = new Sample($elem);
        
        $samples[] = $sample;
    }
    
}
        
  


?>