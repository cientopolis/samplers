<?php
    require_once 'sample.php';

    $sample_id = $_GET["id"];
    
    $sample_dir = "sample_".$sample_id;

    $sample = new Sample($sample_dir);
?>