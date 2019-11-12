<!DOCTYPE html>
<html lang="en">
<head>
  <title>Samplers Viewer</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
  
  <link href="css/jquery.rainbowJSON.css" rel="stylesheet">
  <script src="js/jquery.rainbowJSON.js"></script>
  
</head>
<body>

<?php
    require_once 'sampleDetailsController.php';
?>
  


<div class="jumbotron text-center">
    <h1>Sample Details</h1>
    <p>Sample id: <?php echo $sample->id?></p> 
</div>

<div class="container">
	<button onclick="window.location='index.php'" type="button" class="btn pull-right" ><< Back</button>
	<h2>Sample Details:</h2>  
  
  
	<h3>Files:</h3> 
	<ul>
<?php   foreach ($sample->file_names as $file_name) {  ?>
    	
        <li><?php echo $file_name;?></li>
        
 <?php  } ?>
    </ul>  
    
    <h3>Images:</h3>
			<div id="carouselImages" class="carousel slide" data-ride="carousel">
              
                <!-- Indicators -->
                <ol class="carousel-indicators">
                	<li data-target="#carouselImages" data-slide-to="0" class="active"></li>
<?php                    
                    for ($j=1; $j < sizeof($sample->images); $j++) {  

?>                        
					<li data-target="#carouselImages" data-slide-to="<?php echo $j; ?>"></li>
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
                <a class="left carousel-control" href="#carouselImages" data-slide="prev">
                  <span class="glyphicon glyphicon-chevron-left"></span>
                  <span class="sr-only">Previous</span>
                </a>
                <a class="right carousel-control" href="#carouselImages" data-slide="next">
                  <span class="glyphicon glyphicon-chevron-right"></span>
                  <span class="sr-only">Next</span>
                </a>
              </div>    
    
    
    <h3>JSON:</h3>
<!-- 	<pre> -->
	<div class="json">
	<?php echo $sample->json;?>
	</div>
<!-- 	</pre> -->

</div>  <!-- CONTAINER -->

<script type="text/javascript">
$('.json').rainbowJSON();
</script>

</body>
</html>