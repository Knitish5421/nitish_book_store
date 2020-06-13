/**
 * 
 */

function checkBillingAddress() {
	if ($("#theSameASShippingAddress").is(":checked")) {
		$(".billingAddress").prop("disabled", true);
	} else {
		$(".billingAddress").prop("disabled", false);
	}
}

/*function checkPasswordMatch(){
	var password = $("#txtNewPassword").val();
	var confirmPassword = ${"#txtConfirmPassword").val();
	
	if(password == "" && confirmPassword == ""){
		$("#checkPasswordMatch").html("");
		$("#updateUserInfoButton").prop('disabled', false);
	}else
		{
		if(password != confirmPassword)
			{
			$("#checkPasswordMatch").html("Passwords do not match!");
			$("#updateUserInfoButton").prop('disabled', true);
			}
		else
			{
			$("#checkPasswordMatch").html("Password match");
			$("#updateUserInfoButton").prop('disabled', false);
			}
		}
	}
}*/

$(document).ready(function(){
	$(".cartItemQty").on('change',function(){
		var id=this.id;
		$('#update-item-'+id).css('display','inline-block');
	});
	$("#theSameASShippingAddress").on('click',checkBillingAddress);
/*	$("#txtConfirmPassword").keyup(checkPasswordMatch);
	$("#txtNewPassword").keyup(checkPasswordMatch);*/
});