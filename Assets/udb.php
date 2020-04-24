<?php

define("DB_HOST", "localhost");
define("DB_USER", "root");
define("DB_PASSWORD", "");
define("DB_NAME", "asa");

$db = "asa";

$ownername = $_POST["ownername"];
$regno = $_POST["regno"];
$makermodel = $_POST["makermodel"];

$regdate = $_POST["regdate"];
$fueltype = $_POST["fueltype"];
$vehclass = $_POST["vehclass"];


//$vehage = $_POST["vehage"];
$engineno = $_POST["engineno"];
$chassisno = $_POST["chassisno"];

$fitness = $_POST["fitness"];
$insurance = $_POST["insurance"];



$conn = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);

if($conn)
{
	//echo "Connected.";

	$regnoFirst4 = substr($regno,0,4);
    //echo $regnoFirst4;

    $query = " select district from rtolookup where rtocode like '$regnoFirst4'";
    $result = mysqli_query($conn, $query);

    $rowfound = mysqli_num_rows($result);
    $district = "";

    if($rowfound==1)
    {
        $rows = mysqli_fetch_assoc($result);
        $district = $rows['district'];
        //echo $district;
    }





	//echo $regno . "!!!!";



	$sql = "insert into maintable values ('$ownername', '$regno', '$makermodel', STR_TO_DATE('$regdate','%d-%b-%Y'), '$fueltype', '$vehclass', '$engineno', '$chassisno', STR_TO_DATE('$fitness','%d-%b-%Y'), STR_TO_DATE('$insurance','%d-%b-%Y'), '$district')";
	//$sql = "insert into maintable values ('Pranav', 'Pranav', 'Pranav')";

	if ($conn->query($sql) === TRUE) 
	{
    	echo "Successful.";
	}
	else if (mysqli_errno($conn) == 1062) 
	{
    	echo "Error: Not inserted because this entry already exists.";
	}
	else 
	{
    	echo "Error: " . $sql . "<br>" . $conn->error;
	}
	









}
else
{
	echo "Not connected.";
}




?>