<?php
/*
Template Name: PowerZone - Fitness, Workout & Gym HTML Template | DexignZone

Variable
	$recaptchaSecret : Recaptcha Secret Key
 
	$dzName : Contact Person Name
	$dzEmail : Contact Person Email
	$dzMessage : Contact Person Message
	$dzRes : response holder
	$dzOtherField : Form other additional fields
	
	
	$dzMailSubject : Mail Subject.
	$dzMailMessage : Mail Body
	$dzMailHeader : Mail Header
	$dzEmailReceiver : Contact receiver email address
	$dzEmailFrom : Mail Form title
	$dzEmailHeader : Mail headers
*/
/* require ReCaptcha class */
require('recaptcha-master/src/autoload.php');

/* ReCaptch Secret */
$recaptchaSecret = '<!-- Put Your reCaptcha Secret Key -->';

$dzEmailTo 		= "info@exemple.com";   /* Receiver Email Address */
$dzEmailFrom    = "PowerZone Contact";


function pr($value)
{
	echo "<pre>";
	print_r($value);
	echo "</pre>";
}

try {
    if (!empty($_POST)) {
		
		$dzRes = array('status' => 0, 'msg'=>'Something Went Wrong.');
	
		$reCaptchaEnable = isset($_POST['reCaptchaEnable']) ? $_POST['reCaptchaEnable'] : 1;
		
		if($reCaptchaEnable)
		{
		
			/* validate the ReCaptcha, if something is wrong, we throw an Exception,
				i.e. code stops executing and goes to catch() block */
			
			if (!isset($_POST['g-recaptcha-response'])) {
				$dzRes['status'] = 0;
				$dzRes['msg'] = 'ReCaptcha is not set.';
				echo json_encode($dzRes);
				exit;
			}

			/* do not forget to enter your secret key from https://www.google.com/recaptcha/admin */
			
			$recaptcha = new \ReCaptcha\ReCaptcha($recaptchaSecret, new \ReCaptcha\RequestMethod\CurlPost());
			
			/* we validate the ReCaptcha field together with the user's IP address */
			
			$response = $recaptcha->verify($_POST['g-recaptcha-response'], $_SERVER['REMOTE_ADDR']);

			if (!$response->isSuccess()) {
				$dzRes['status'] = 0;
				$dzRes['msg'] = 'ReCaptcha was not validated.';
				echo json_encode($dzRes);
				exit;
			}
        }
		
		#### Contact Form Script ####
		if($_POST['dzToDo'] == 'Contact')
		{
			$error 			= false;
			$dzName			= !empty($_POST['dzName'])?trim(strip_tags($_POST['dzName'])):'';
			$dzEmail		= !empty($_POST['dzEmail'])?trim(strip_tags($_POST['dzEmail'])):'';
			$dzMessage		= !empty($_POST['dzMessage'])?strip_tags($_POST['dzMessage']):'';

			if(empty($dzName)){
				
				if(isset($_POST['dzFirstName']) || isset($_POST['dzLastName'])){
					if(empty($_POST['dzFirstName']) || empty($_POST['dzLastName'])){
							$error	=	true;
							$msg	=	'Please fill name.';
					}else{
						$dzName = $_POST['dzFirstName'].' '.$_POST['dzLastName'];
					}
				}else{
					$error	=	true;
					$msg	=	'Please fill name.';	
				}
				
			}else if(!isAlphabetic($dzName)){
				$error = true;
				$msg	=	'Please enter alphbetic  characters.';
			}
			
			$dzPhoneMessage  = '';
			if(isset($_POST['dzPhoneNumber'])){
				
				$dzPhoneNumber = !empty($_POST['dzPhoneNumber']) ? $_POST['dzPhoneNumber']:'';
				
				if(empty($dzPhoneNumber)){
					$error = true;
					$msg   = 'Please enter phone number.';	
				}else if(!isValidPhonenumber($dzPhoneNumber)){
					$error = true;
					$msg   = 'Please enter valid phone number.';
				}
				
				if(!empty($dzPhoneNumber)){
					$dzPhoneMessage = "Phone Number: $dzPhoneNumber <br/>";
				}
			}
			
			if(empty($dzEmail)){
				$error = true;
				$msg = 'Please enter email.';
			}else if (!filter_var($dzEmail, FILTER_VALIDATE_EMAIL)) 
			{
				$error	=	true;
				$msg	= 'Wrong Email Format.';
			}
			
			if(empty($dzMessage)){
				$error = true;
				$msg = 'Please enter message.';
			}
			
			if ($error) {
				$dzRes['msg'] = $msg;
				echo json_encode($dzRes);
				exit;
			}
			
			$dzMailSubject = 'PowerZone|Contact Form: A Person want to contact';
			$dzMailMessage	= 	"
								A person want to contact you: <br><br>
								Name: $dzName<br/>
								Email: $dzEmail<br/>".$dzPhoneMessage."
								Message: $dzMessage<br/>
								";
								
			$dzOtherField = "";
			if(!empty($_POST['dzOther']))
			{
				$dzOther = $_POST['dzOther'];
				$message = "";
				foreach($dzOther as $key => $value)
				{
					$fieldName = ucfirst(str_replace('_',' ',$key));
					$fieldValue = ucfirst(str_replace('_',' ',$value));
					$dzOtherField .= $fieldName." : ".$fieldValue."<br>";
				}
			}
			$dzMailMessage .= $dzOtherField; 
								
			$dzEmailHeader  	= "MIME-Version: 1.0\r\n";
			$dzEmailHeader 		.= "Content-type: text/html; charset=iso-8859-1\r\n";
			$dzEmailHeader 		.= "From:$dzEmailFrom <$dzEmail>";
			$dzEmailHeader 		.= "Reply-To: $dzEmail\r\n"."X-Mailer: PHP/".phpversion();
			if(mail($dzEmailTo, $dzMailSubject, $dzMailMessage, $dzEmailHeader))
			{
				$dzRes['status'] = 1;
				$dzRes['msg'] = 'We have received your message successfully. Thanks for Contact.';
			}
			else
			{
				$dzRes['status'] = 0;
				$dzRes['msg'] = 'Some problem in sending mail, please try again later.';
			}
			echo json_encode($dzRes);
			exit;
		}
		#### Contact Form Script End ####
		
		#### Appointment Form Script ####
		if($_POST['dzToDo'] == 'Appointment')
		{
			$error 			= false;
			$dzName			= !empty($_POST['dzName'])?trim(strip_tags($_POST['dzName'])):'';
			$dzEmail		= !empty($_POST['dzEmail'])?trim(strip_tags($_POST['dzEmail'])):'';
			$dzMessage		= !empty($_POST['dzMessage'])?strip_tags($_POST['dzMessage']):'';

			if(empty($dzName)){
				
				if(isset($_POST['dzFirstName']) || isset($_POST['dzLastName'])){
					if(empty($_POST['dzFirstName']) || empty($_POST['dzLastName'])){
							$error	=	true;
							$msg	=	'Please fill name.';
					}else{
						$dzName = $_POST['dzFirstName'].' '.$_POST['dzLastName'];
					}
				}else{
					$error	=	true;
					$msg	=	'Please fill name.';	
				}
				
			}else if(!isAlphabetic($dzName)){
				$error = true;
				$msg	=	'Please enter alphbetic  characters.';
			}
			
			$dzPhoneMessage  = '';
			if(isset($_POST['dzPhoneNumber'])){
				
				$dzPhoneNumber = !empty($_POST['dzPhoneNumber']) ? $_POST['dzPhoneNumber']:'';
				
				if(empty($dzPhoneNumber)){
					$error = true;
					$msg   = 'Please enter phone number.';	
				}else if(!isValidPhonenumber($dzPhoneNumber)){
					$error = true;
					$msg   = 'Please enter valid phone number 12 digit with country code.';
				}
				
				if(!empty($dzPhoneNumber)){
					$dzPhoneMessage = "Phone Number: $dzPhoneNumber <br/>";
				}
			}
			
			if(empty($dzEmail)){
				$error = true;
				$msg = 'Please enter email.';
			}else if (!filter_var($dzEmail, FILTER_VALIDATE_EMAIL)) 
			{
				$error	=	true;
				$msg	= 'Wrong Email Format.';
			}
			
			if(empty($dzMessage)){
				$error = true;
				$msg = 'Please enter message.';
			}
			
			if ($error) {
				$dzRes['msg'] = $msg;
				echo json_encode($dzRes);
				exit;
			}
			
				
			
			$dzMailSubject = 'PowerZone|Appointment Form: A Person want to contact';
			$dzMailMessage	= 	"
								A person want to contact you: <br><br>
								Name: $dzName<br/>
								Email: $dzEmail<br/>".$dzPhoneMessage."
								Message: $dzMessage<br/>
								";
			$dzOtherField = "";
			if(!empty($_POST['dzOther']))
			{
				$dzOther = $_POST['dzOther'];
				$message = "";
				foreach($dzOther as $key => $value)
				{
					$fieldName = ucfirst(str_replace('_',' ',$key));
					$fieldValue = ucfirst(str_replace('_',' ',$value));
					$dzOtherField .= $fieldName." : ".$fieldValue."<br>";
				}
			}
			$dzMailMessage .= $dzOtherField; 
			
			$dzEmailHeader  	= "MIME-Version: 1.0\r\n";
			$dzEmailHeader 		.= "Content-type: text/html; charset=iso-8859-1\r\n";
			$dzEmailHeader 		.= "From:$dzEmailFrom <$dzEmail>";
			$dzEmailHeader 		.= "Reply-To: $dzEmail\r\n"."X-Mailer: PHP/".phpversion();
			if(mail($dzEmailTo, $dzMailSubject, $dzMailMessage, $dzEmailHeader))
			{
				$dzRes['status'] = 1;
				$dzRes['msg'] = 'We have received your message successfully. Thanks for Contact.';
			}
			else
			{
				$dzRes['status'] = 0;
				$dzRes['msg'] = 'Some problem in sending mail, please try again later.';
			}
			echo json_encode($dzRes);
			exit;
		}	
		#### Appointment Form Script End ####
		
	}
} catch (\Exception $e) {
    $dzRes['status'] = 0;
	$dzRes['msg'] = $e->getMessage().'Some problem in sending mail, please try again later.';
	echo json_encode($dzRes);
	exit;
}

function isAlphabetic($data){
	if (!preg_match('/[^A-Za-z 0-9]/', $data)) // '/[^a-z\d]/i' should also work.
	{
		return true;
	} else{
		return false;
	}
}

function isValidPhonenumber($phone_number){
	if(preg_match('/^[0-9]{10}+$/', $phone_number)) {
		return true;
	} else{
		return false;
	}
}