<!DOCTYPE html>
<html lang="en">
<head>
  <title>Samplers Viewer</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
</head>
<body>


<div class="jumbotron text-center">
    <h1>Samplers</h1>
    <p>Test page to see the uploaded samples</p> 
</div>
    
<div class="container">    
    <h2>Samples List:</h2>  
    <div class="row">
  
<?php   
    require_once 'indexController.php';

    foreach ($samples as $i => $sample) {

?>  
      <div class="col-md-4">
          <div class="thumbnail">
            <a href="sampleDetails.php?id=<?php echo $sample->id;?>">
            
              <div id="myCarousel<?php echo $i;?>" class="carousel slide" data-ride="carousel">
              
                <!-- Indicators -->
                <ol class="carousel-indicators">
                	<li data-target="#myCarousel<?php echo $i;?>" data-slide-to="0" class="active"></li>
<?php                    
                    for ($j=1; $j < sizeof($sample->images); $j++) {  

?>                        
					<li data-target="#myCarousel<?php echo $i;?>" data-slide-to="<?php echo $j; ?>"></li>
<?php

                    }
?>
                </ol>
            
                <!-- Wrapper for slides -->
                <div class="carousel-inner">
<?php                    
                    foreach ($sample->images as $j => $image) {
?>    
                
                          <div class="item <?php if ($j == 0) { ?>active <?php }?>">
                            <img src="<?php echo $image;?>" alt="Image" style="width:100%;">
                          </div>
                             
<?php 
                    }
?>                  
                  
                </div>
            
                <!-- Left and right controls -->
                <a class="left carousel-control" href="#myCarousel<?php echo $i;?>" data-slide="prev">
                  <span class="glyphicon glyphicon-chevron-left"></span>
                  <span class="sr-only">Previous</span>
                </a>
                <a class="right carousel-control" href="#myCarousel<?php echo $i;?>" data-slide="next">
                  <span class="glyphicon glyphicon-chevron-right"></span>
                  <span class="sr-only">Next</span>
                </a>
              </div>
              
              <div class="caption">
                <p>Sample: <?php echo $sample->id;?></p>
              </div>
            </a>
          </div>
        </div>
        
<?php                 
        
    }
    
?> 

        
	</div> <!-- ROW -->
	
  
</div>  <!-- CONTAINER -->

</body>
</html>
