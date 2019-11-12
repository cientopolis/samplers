<?php

class Sample {
    
    public static $SAMPLES_DIR = "./uploads";
    
    public $id;
    public $json;
    public $file_names;
    public $images;
    
    
    function __construct($sample_dir) {
        
        $this->id = substr($sample_dir,strpos($sample_dir,"_")+1,strlen($sample_dir));
        $this->json = "";
        $this->file_names = [];
        $this->images = [];
        
        
        $directory_sample = Sample::$SAMPLES_DIR."/".$sample_dir;
        
        $list_sample = scandir($directory_sample);
        
        
        foreach ($list_sample as $elem_sample) {
            
            
            if (($elem_sample != ".") && ($elem_sample != "..")) {
                
                $this->file_names[] = $elem_sample;
                
                $ext = pathinfo($directory_sample."/".$elem_sample, PATHINFO_EXTENSION);
                
                if ($ext == "jpg") {
                    $this->images[] = $directory_sample."/".$elem_sample;
                }
                else if ($ext == "json") {
                    $this->json = file_get_contents($directory_sample."/".$elem_sample);
                    $this->json = str_replace(array("\n", "\r"), '', $this->json);
                }
            }
        }
        
        
    }
}




?>