/**
	Template Name 	 : PowerZone
	Author			 : DexignZone
	Version			 : 1.0
	File Name	     : custom.js
	Author Portfolio : https://themeforest.net/user/dexignzone/portfolio
	
	Core script to handle the entire theme and core functions
	
**/

var PowerZone = function(){

	/* Search Bar ============ */
	siteUrl = '';
	
	var screenWidth = $( window ).width();
	
	/* Home Search ============ */
	var handleHomeSearch = function() {
		'use strict';
		/* top search in header on click function */
		var quikSearch = jQuery("#quik-search-btn");
		var quikSearchRemove = jQuery("#quik-search-remove");
		
		quikSearch.on('click',function() {
			jQuery('.dz-quik-search').fadeIn(500);
			jQuery('.dz-quik-search').addClass('On');
		});
		
		quikSearchRemove.on('click',function() {
			jQuery('.dz-quik-search').fadeOut(500);
			jQuery('.dz-quik-search').removeClass('On');
		});	
		/* top search in header on click function End*/
	}
	
	/* Header Height ============ */
	var handleResizeElement = function(){
		var headerTop = 0;
		var headerNav = 0;
		
		$('.header .sticky-header').removeClass('is-fixed');
		$('.header').removeAttr('style');
		
		if(jQuery('.header .top-bar').length > 0 &&  screenWidth > 991)
		{
			headerTop = parseInt($('.header .top-bar').outerHeight());
		}

		if(jQuery('.header').length > 0 )
		{
			headerNav = parseInt($('.header').height());
			headerNav =	(headerNav == 0)?parseInt($('.header .main-bar').outerHeight()):headerNav;
		}	
		
		var headerHeight = headerNav + headerTop;
		
		jQuery('.header').css('height', headerHeight);		
	}
	
	/* Resize Element On Resize ============ */
	var handleResizeElementOnResize = function(){
		var headerTop = 0;
		var headerNav = 0;
		
		$('.header .sticky-header').removeClass('is-fixed');
		$('.header').removeAttr('style');
		
		
		setTimeout(function(){
			
			if(jQuery('.header .top-bar').length > 0 &&  screenWidth > 991)
			{
				headerTop = parseInt($('.header .top-bar').outerHeight());
			}

			if(jQuery('.header').length > 0 )
			{
				headerNav = parseInt($('.header').height());
				headerNav =	(headerNav == 0)?parseInt($('.header .main-bar').outerHeight()):headerNav;
			}	
			
			var headerHeight = headerNav + headerTop;
			
			jQuery('.header').css('height', headerHeight);
		
		}, 500);
    }
	
	
	
	/* Range ============ */
	var priceslider = function(){
		if($("#slider-tooltips").length > 0 ) {
			var tooltipSlider = document.getElementById('slider-tooltips');
			
			var formatForSlider = {
				from: function (formattedValue) {
					return Number(formattedValue);
				},
				to: function(numericValue) {
					return Math.round(numericValue);
				}
			};

			noUiSlider.create(tooltipSlider, {
				start: [40, 346],
				connect: true,
				format: formatForSlider,
				tooltips: [wNumb({decimals: 1}), true],
				range: {
					'min': 0,
					'max': 400
				}
			});
			var formatValues = [
				document.getElementById('slider-margin-value-min'),
				document.getElementById('slider-margin-value-max')
			];
			tooltipSlider.noUiSlider.on('update', function (values, handle, unencoded) {
				formatValues[0].innerHTML = "Min Price: " + "$"+ values[0];
				formatValues[1].innerHTML = "Max Price: " + "$"+ values[1];
			});
		}
	}
	
	/* Load File ============ */
	var handleDzTheme = function(){
		'use strict';
		var loadingImage = '<img src="images/loading.gif">';
		jQuery('.dzload').each(function(){
		var dzsrc =   siteUrl + $(this).attr('dzsrc');
		  	jQuery(this).hide(function(){
				jQuery(this).load(dzsrc, function(){
					jQuery(this).fadeIn('slow');
				}); 
			})
		});
		
		if(screenWidth <= 991 ){
			jQuery('.navbar-nav > li > a, .sub-menu > li > a').unbind().on('click', function(e){
				if(jQuery(this).parent().hasClass('open'))
				{
					jQuery(this).parent().removeClass('open');
				}
				else{
					jQuery(this).parent().parent().find('li').removeClass('open');
					jQuery(this).parent().addClass('open');
				}
			});
		}
		
		jQuery('.sidenav-nav .navbar-nav > li > a').next('.sub-menu,.mega-menu').slideUp();
		jQuery('.sidenav-nav .sub-menu > li > a').next('.sub-menu').slideUp();
		
		jQuery('.sidenav-nav .navbar-nav > li > a, .sidenav-nav .sub-menu > li > a').unbind().on('click', function(e){
			if(jQuery(this).hasClass('dz-open')){
				jQuery(this).removeClass('dz-open');
				jQuery(this).parent('li').children('.sub-menu,.mega-menu').slideUp();
			}else{
				jQuery(this).addClass('dz-open');
				
				if(jQuery(this).parent('li').children('.sub-menu,.mega-menu').length > 0){
					
					e.preventDefault();
					jQuery(this).next('.sub-menu,.mega-menu').slideDown();
					jQuery(this).parent('li').siblings('li').find('a').removeClass('dz-open');
					jQuery(this).parent('li').siblings('li').children('.sub-menu,.mega-menu').slideUp();
				}else{
					jQuery(this).next('.sub-menu,.mega-menu').slideUp();
				}
			}
		});
	}
	
	/* Magnific Popup ============ */
	var handleMagnificPopup = function(){
		'use strict';	
		
		if(jQuery('.mfp-gallery').length > 0){
			/* magnificPopup function */
			jQuery('.mfp-gallery').magnificPopup({
				delegate: '.mfp-link',
				type: 'image',
				tLoading: 'Loading image #%curr%...',
				mainClass: 'mfp-img-mobile',
				gallery: {
					enabled: true,
					navigateByImgClick: true,
					preload: [0,1] // Will preload 0 - before current, and 1 after the current image
				},
				image: {
					tError: '<a href="%url%">The image #%curr%</a> could not be loaded.',
					titleSrc: function(item) {
						return item.el.attr('title') + '<small></small>';
					}
				}
			});
			/* magnificPopup function end */
		}
		
		if(jQuery('.mfp-video').length > 0){
			/* magnificPopup for Play video function */		
			jQuery('.mfp-video').magnificPopup({
				type: 'iframe',
				iframe: {
					markup: '<div class="mfp-iframe-scaler">'+
							'<div class="mfp-close"></div>'+
							'<iframe class="mfp-iframe" frameborder="0" allowfullscreen></iframe>'+
							'<div class="mfp-title">Some caption</div>'+
							'</div>'
				},
				callbacks: {
					markupParse: function(template, values, item) {
						values.title = item.el.attr('title');
					}
				}
			});	
		}

		if(jQuery('.popup-youtube, .popup-vimeo, .popup-gmaps').length > 0){
			/* magnificPopup for Play video function end */
			$('.popup-youtube, .popup-vimeo, .popup-gmaps').magnificPopup({
				disableOn: 700,
				type: 'iframe',
				mainClass: 'mfp-fade',
				removalDelay: 160,
				preloader: false,
				fixedContentPos: false
			});
		}
	}
	
	
	/* Header Fixed ============ */
	var handleHeaderFix = function(){
		'use strict';
		/* Main navigation fixed on top  when scroll down function custom */		
		jQuery(window).on('scroll', function () {
			if(jQuery('.sticky-header').length > 0){
				var menu = jQuery('.sticky-header');
				if ($(window).scrollTop() > menu.offset().top) {
					menu.addClass('is-fixed');
				} else {
					menu.removeClass('is-fixed');
				}
			}
		});
		/* Main navigation fixed on top  when scroll down function custom end*/		
	}
	
	/* Masonry Box ============ */
	var handleMasonryBox = function(i){
		
		/* masonry by  = bootstrap-select.min.js */
		var masonryId 		= '#masonry'+i;
		var masonryClass 	= '.masonry'+i;
		var filtersClass 	= '.filters'+i;

		if(jQuery(masonryId +', '+ masonryClass).length > 0){
			jQuery(filtersClass+' li').removeClass('active');
			jQuery(filtersClass+' li:first').addClass('active');
			var self = jQuery(masonryId +', '+ masonryClass);
			var filterValue = "";
	 
			if(jQuery('.card-container').length > 0){
				var gutterEnable = self.data('gutter');
				
				var gutter = (self.data('gutter') === undefined)?0:self.data('gutter');
				gutter = parseInt(gutter);
				
				
				var columnWidthValue = (self.attr('data-column-width') === undefined)?'':self.attr('data-column-width');
				if(columnWidthValue != ''){columnWidthValue = parseInt(columnWidthValue);}
				
				self.imagesLoaded(function () {
					filter: filterValue,
					self.masonry({
						gutter: gutter,
						columnWidth:columnWidthValue, 
						//columnWidth:3, 
						//gutterWidth: 15,
						isAnimated: true,
						itemSelector: ".card-container",
						//horizontalOrder: true,
						//fitWidth: true,
						//stagger: 30
						//containerStyle: null
						//percentPosition: true
					});
					
				}); 
			} 
		}
		if(jQuery(filtersClass).length){
			jQuery(filtersClass+" li:first").addClass('active');
			
			jQuery(filtersClass).on("click", "li", function() {
				
				jQuery(filtersClass+' li').removeClass('active');
				jQuery(this).addClass('active');
				
				var filterValue = $(this).attr("data-filter");
				self.isotope({ 
					filter: filterValue,
					masonry: {
						gutter: gutter,
						columnWidth: columnWidthValue,
						isAnimated: true,
						itemSelector: ".card-container"
					}
				});
			});
		}
		/* masonry by  = bootstrap-select.min.js end */
	}
	
	var handleMasonryBoxLoop = function(){
		for(var i=1; i <= 10; i++)
		{
			handleMasonryBox(i);
		}
	}
	

	/* Counter Number ============ */
	var handleCounter = function(){
		if(jQuery('.counter').length){
			jQuery('.counter').counterUp({
				delay: 10,
				time: 3000
			});	
		}
	}
	
	/* Video Popup ============ */
	var handleVideo = function(){
		/* Video responsive function */	
		jQuery('iframe[src*="youtube.com"]').wrap('<div class="embed-responsive embed-responsive-16by9"></div>');
		jQuery('iframe[src*="vimeo.com"]').wrap('<div class="embed-responsive embed-responsive-16by9"></div>');	
		/* Video responsive function end */
	}
	
	/* Gallery Filter ============ */
	var handleFilterMasonary = function(){
		/* gallery filter activation = jquery.mixitup.min.js */ 
		if (jQuery('#image-gallery-mix').length) {
			jQuery('.gallery-filter').find('li').each(function () {
				$(this).addClass('filter');
			});
			jQuery('#image-gallery-mix').mixItUp();
		};
		if(jQuery('.gallery-filter.masonary').length){
			jQuery('.gallery-filter.masonary').on('click','span', function(){
				var selector = $(this).parent().attr('data-filter');
				jQuery('.gallery-filter.masonary span').parent().removeClass('active');
				jQuery(this).parent().addClass('active');
				jQuery('#image-gallery-isotope').isotope({ filter: selector });
				return false;
			});
		}
		/* gallery filter activation = jquery.mixitup.min.js */
	}
	
	/* BGEFFECT ============ */
	var reposition = function (){
		'use strict';
		var modal = jQuery(this),
		dialog = modal.find('.modal-dialog');
		modal.css('display', 'block');
		
		/* Dividing by two centers the modal exactly, but dividing by three 
		 or four works better for larger screens.  */
		dialog.css("margin-top", Math.max(0, (jQuery(window).height() - dialog.height()) / 2));
	}
	
	var handelResize = function (){
		/* Reposition when the window is resized */
		jQuery(window).on('resize', function() {
			jQuery('.modal:visible').each(reposition);			
		});
	}
	
	// Light Gallery ============
	var handleLightgallery = function() {
		if(jQuery('#lightgallery').length > 0){
			lightGallery(document.getElementById('lightgallery'), {
				plugins: [lgThumbnail, lgZoom],
				selector: '.lg-item',
				thumbnail:true,
				exThumbImage: 'data-src'
            });
		}
		if(jQuery('#lightgallery2').length > 0){
			lightGallery(document.getElementById('lightgallery2'), {
				plugins: [lgThumbnail, lgZoom],
				selector: '.lg-item',
				thumbnail:true,
				exThumbImage: 'data-src'
            });
		}
		if(jQuery('#lightgallery3').length > 0){
			lightGallery(document.getElementById('lightgallery3'),{
				plugins: [lgThumbnail, lgZoom],
				selector: '.lg-item',
				thumbnail:true,
				exThumbImage: 'data-src'
            });
		}
		if(jQuery('#lightgallery4').length > 0){
			lightGallery(document.getElementById('lightgallery4'),{
				plugins: [lgThumbnail, lgZoom],
				selector: '.lg-item',
				thumbnail:true,
				exThumbImage: 'data-src'
            });
		}
		if(jQuery('#lightgallery5').length > 0){
			lightGallery(document.getElementById('lightgallery5'),{
				plugins: [lgThumbnail, lgZoom],
				selector: '.lg-item',
				thumbnail:true,
				exThumbImage: 'data-src'
            });
		}
	}
	
	/* handle Bootstrap Touch Spin ============ */
	var handleBootstrapTouchSpin = function(){
		if($("input[name='demo_vertical2']").length > 0 ) {
			jQuery("input[name='demo_vertical2']").TouchSpin({
			  verticalbuttons: true,
			  verticalupclass: 'fa-solid fa-plus',
			  verticaldownclass: 'fa-solid fa-minus'
			});
		}
	 	if($(".quantity-input").length > 0 ) {
			jQuery(".quantity-input").TouchSpin({
			  verticalbuttons: true,
			  verticalupclass: 'fa-solid fa-plus',
			  verticaldownclass: 'fa-solid fa-minus'
			});
		}
	}
	
	var handleMagnifyGallery = function(){
		
		const imageSelector = $('.DZoomImage');
		/* let x = imageSelector.find('img');
		$(window).on('resize',function(){
			console.log(imageSelector.width());
			x.css("width", imageSelector.width() + "px");
			x.css("height", imageSelector.height() + "px");
		}) */



		imageSelector.on('mousemove',function(t) {
			let e = $(this).offset();
			var i = (t.pageX - e.left) / $(this).width() * 100 <= 100 ? (t.pageX - e.left) / $(this).width() * 100 : 100;
			var c = (t.pageY - e.top) / $(this).height() * 100 <= 100 ? (t.pageY - e.top) / $(this).height() * 100 : 100;
			
			$(this).find('img').css("transform-origin", i + "% " + c + "%");
		})
		imageSelector.on('mouseenter',function(t) {
			let n = $(this).find('img');
			n.css("cursor", "pointer"), 
				/* n.css("width", $(this).width() + "px"), 
				n.css("height", $(this).height() + "px"), */ 
			n.css("transition", "0.1s"), 
			n.css("transform", "scale(" + 1.5 + ")"),
			$(this).find('.mfp-link i').css({opacity : 1,zIndex : 1})
		});
		imageSelector.on('mouseleave',function(t) {
			let n = $(this).find('img');
			n.css("transition", "0.1s"), n.css("transform", "scale(1)")
			$(this).find('.mfp-link i').css({opacity : 0,zIndex : 1})
		});
	}

	/* Box Hover ============ */
	var handleBoxHover = function(){
		jQuery('.box-hover').on('mouseenter',function(){
			var selector = jQuery(this).parent().parent();
			selector.find('.box-hover').removeClass('active');
			jQuery(this).addClass('active');
		});
	}


	/* Current Active Menu ============ */
	var handleCurrentActive = function() {
		for (var nk = window.location,
				o = $("ul.navbar a").filter(function(){
				return this.href == nk;
			})
			.addClass("active").parent().addClass("active");;)
		{
		if (!o.is("li")) break;
			o = o.parent().addClass("show").parent('li').addClass("active");
		}
	}
	
	/* Select Picker ============ */
	var handleSelectpicker = function(){
		if(jQuery('.default-select').length > 0 ){
			jQuery('.default-select').selectpicker();
		}
	}

	/* Handle Placeholder ============ */
	var handlePlaceholderAnimation = function(){
		if(jQuery('.dz-form').length){
			$('input, textarea').focus(function(){
				$(this).parents('.input-area').addClass('focused');
			});
			$('input, textarea').blur(function(){
				var inputValue = $(this).val();
				if ( inputValue == "" ) {
					$(this).removeClass('filled');
					$(this).parents('.input-area').removeClass('focused');  
				} else {
					$(this).addClass('filled');
				}
			})
		}
	}

	/* Icon Dropdowm ============ */
	var handleIconDropdowm = function(){
		jQuery(".icon-dropdown").on('click', function(){
			if($(this).hasClass("show")){
				$(this).removeClass("show");
			}else {
				jQuery(".icon-dropdown").removeClass("show");	
				$(this).addClass("show");
			}
	  	});
	}
	
	/* Perfect Scrollbar ============ */
	var handlePerfectScrollbar = function() {
		if(jQuery('.deznav-scroll').length > 0){
			const qs = new PerfectScrollbar('.deznav-scroll');
			qs.isRtl = false;
		}
	}

	/* Wow Animation ============ */
	var handleWowAnimation = function(){
		if($('.wow').length > 0){
			var wow = new WOW({
				boxClass:     'wow',      
				animateClass: 'animated', 
				offset:       0,          
				mobile:       false,     
			});
			setTimeout(function(){
				wow.init();	
			}, 2100);
		}
	}
	
	/* Countdown Timer ============ */
	var handleFinalCountDown = function(){
		var launchDate = jQuery('.countdown-timer').data('date');
		
		if(launchDate != undefined && launchDate != '')
		{
			WebsiteLaunchDate = launchDate;
		}

		if(jQuery('.countdown-timer').length > 0 )
		{
			var startTime = new Date(); // Put your website start time here
			startTime = startTime.getTime();
			
			var currentTime = new Date();
			currentTime = currentTime.getTime();
			
			var endTime = new Date(WebsiteLaunchDate); // Put your website end time here			
			endTime = endTime.getTime();		
			
			$('.countdown-timer').final_countdown({
				
				'start': (startTime/1000),
				'end': (endTime/1000), 
				'now': (currentTime/1000), 
				selectors: {
					value_seconds:'.clock-seconds .val',
					canvas_seconds:'canvas-seconds',
					value_minutes:'.clock-minutes .val',
					canvas_minutes:'canvas-minutes',
					value_hours:'.clock-hours .val',
					canvas_hours:'canvas-hours',
					value_days:'.clock-days .val',
					canvas_days:'canvas-days'
				},
				seconds: {
					borderColor:$('.type-seconds').attr('data-border-color'),
					borderWidth:'5',
				},
				minutes: {
					borderColor:$('.type-minutes').attr('data-border-color'),
					borderWidth:'5'
				},
				hours: {
					borderColor:$('.type-hours').attr('data-border-color'),
					borderWidth:'5'
				},
				days: {
					borderColor:$('.type-days').attr('data-border-color'),
					borderWidth:'5'
				}
			}, function() {
				jQuery.ajax({
					type: 'POST',
					url: akcel_js_data.admin_ajax_url,
					data: "action=change_theme_status_ajax&security="+akcel_js_data.ajax_security_nonce,
					success: function(data) {
						location.reload();
					}
				});				
			});
		}
	}	
  
  	var WebsiteLaunchDate = new Date();
	var monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
	WebsiteLaunchDate.setMonth(WebsiteLaunchDate.getMonth() + 1);
	WebsiteLaunchDate =  WebsiteLaunchDate.getDate() + " " + monthNames[WebsiteLaunchDate.getMonth()] + " " + WebsiteLaunchDate.getFullYear();

	
	/* BMI Calculator ============ */
	var handleBmiCalculator = function(){
		if(jQuery('#BmiCalculator').length > 0 ){
			jQuery("#BmiCalculator").on('submit', function() {
				event.preventDefault();
				var age = jQuery("#age").val();
				var height = jQuery("#height").val();
				var weight = jQuery("#weight").val();
				var gender = jQuery("#gender").val();
				var form = jQuery(this);

				if(age == '' || height == '' || weight == '' || gender == '' || gender == false){
					alert("All fields are required!");
					return false;
				}

				form[0].reset();
				var bmi = Number(weight)/(Number(height)/100*Number(height)/100);
				
				var result = '';
				if(bmi < 18.5) {
					result = 'Underweight';
				} else if(18.5 <= bmi && bmi <= 24.9) {
					result = 'Healthy';
				} else if(25 <= bmi && bmi <= 29.9) {
					result = 'Overweight';
				} else if(30 <= bmi && bmi <= 34.9) {
					result = 'Obese';
				} else if(35 <= bmi) {
					result = 'Extremely obese';
				}

				jQuery('.dzFormBmi').html('<div class="dzFormInner"><h4 class="title text-white">'+result+'</h4><h5 class="bmi-result text-primary m-b0">BMI: '+parseFloat(bmi).toFixed(2)+'</h5></div>');
			});
		}
	}
	
	/* smartWizard ============ */
	var smartWizard = function (){
		if(($('#smartwizard').length > 0)){
			// Leave step event is used for validating the forms
			$("#smartwizard").on("leaveStep", function(e, anchorObject, currentStepIdx, nextStepIdx, stepDirection) {
			
                // Validate only on forward movement  
                if (stepDirection == 'forward') {
                  let form = document.getElementById('form-' + currentStepIdx);
				 

				if (form) {
                    if (!form.checkValidity()) {
                      form.classList.add('was-validated');
                      $('#smartwizard').smartWizard("setState", [currentStepIdx], 'error');
                      $("#smartwizard").smartWizard('fixHeight');
                      return false;
                    }
                    $('#smartwizard').smartWizard("unsetState", [currentStepIdx], 'error');
                  }

				  if (currentStepIdx == 0)
				  {
					// get request to validate, to /api/sendSms?phone=...
					//if code == 200, then we validate
					req = new XMLHttpRequest();
					req.open('GET', '/api/sendSms?phone=' + document.getElementById('user_numTel').value, false);
					req.send(null);
					if (req.status != 200)
					{
						document.getElementById("error").style.display = "block";
						document.getElementById("error").innerHTML = "Error sending SMS, please try again later.";
						setTimeout(function() {
							$("#error").fadeOut().empty();
						}, 3000);
						form.classList.add('was-validated');
						$('#smartwizard').smartWizard("setState", [currentStepIdx], 'error');
						$("#smartwizard").smartWizard('fixHeight');
						return false;
					}
				}else if (currentStepIdx == 1)
				  {
					// get request to validate, to /api/verifyPhone?phone=...&code=...
					//if code == 200, then we validate
					req = new XMLHttpRequest();
					req.open('GET', '/api/verifyPhone?phone=' + document.getElementById('user_numTel').value + '&code=' + document.getElementById('code').value, false);
					req.send(null);
					if (req.status != 200)
					{
						document.getElementById("error").style.display = "block";
						document.getElementById("error").innerHTML = "Error validating code, please try again.";
						setTimeout(function() {
							$("#error").fadeOut().empty();
						}, 3000);
						document.getElementById('code').value = '';
						form.classList.add('was-validated');
						$('#smartwizard').smartWizard("setState", [currentStepIdx], 'error');
						$("#smartwizard").smartWizard('fixHeight');
						return false;
					}else
					{
						if (req.responseText == 'approved')
						{
							//do nothing
						}else
						{
							document.getElementById("error").style.display = "block";
							document.getElementById("error").innerHTML = "Invalid code, please try again.";
							setTimeout(function() {
								$("#error").fadeOut().empty();
							}, 3000);
							document.getElementById('code').value = '';
							form.classList.add('was-validated');
							$('#smartwizard').smartWizard("setState", [currentStepIdx], 'error');
							$("#smartwizard").smartWizard('fixHeight');
							return false;
						}
					}
				  }
                }
            });
			// Step show event
			$("#smartwizard").on("showStep", function(e, anchorObject, stepNumber, stepDirection, stepPosition) {
				
			   //alert("You are on step "+stepNumber+" now");
			   if (stepNumber == 2){
				history.replaceState(null, null, ' '); 	

				document.getElementById('form-0').submit();
				//remove hash from url
				
			   }
			   if(stepPosition === 'first'){
				   $("#prev-btn").addClass('disabled');
			   }else if(stepPosition === 'final'){
				   $("#next-btn").addClass('disabled');
				   $("#prev-btn").addClass('disabled');
			   }else{
				   $("#prev-btn").removeClass('disabled');
				   $("#next-btn").removeClass('disabled');
			   }
			});

			// Toolbar extra buttons
			var btnFinish = $('<button></button>').text('Finish')
				.addClass('btn btn-info')
				.on('click', function(){ alert('Finish Clicked'); });
			var btnCancel = $('<button></button>').text('Cancel')
				.addClass('btn btn-danger')
				.on('click', function(){ $('#smartwizard').smartWizard("reset"); });


			// Smart Wizard
			$('#smartwizard').smartWizard({
				selected: 0,
				theme: 'default',
				transitionEffect:'fade',
				showStepURLhash: true,
				toolbarSettings: {toolbarPosition: 'both',
					toolbarButtonPosition: 'end',
					toolbarExtraButtons: [btnFinish, btnCancel]
				}
			});
			
			// External Button Events
			$("#reset-btn").on("click", function() {
				// Reset wizard
				$('#smartwizard').smartWizard("reset");
				return true;
			});

			$("#prev-btn").on("click", function() {
				// Navigate previous
				$('#smartwizard').smartWizard("prev");
				return true;
			});

			$("#next-btn").on("click", function() {
				// Navigate next
				$('#smartwizard').smartWizard("next");
				return true;
			});

			$("#theme_selector").on("change", function() {
				// Change theme
				$('#smartwizard').smartWizard("theme", $(this).val());
				return true;
			});

			// Set selected theme on page refresh
			$("#theme_selector").change();
			
		}
	}
	/* Load File ============ */
	var dzTheme = function () {

		if (screenWidth <= 991) {
			jQuery('.navbar-nav > li > a, .sub-menu > li > a').unbind().on('click', function (e) {
				if (jQuery(this).parent().hasClass('open')) {
					jQuery(this).parent().removeClass('open');
				}
				else {
					jQuery(this).parent().parent().find('li').removeClass('open');
					jQuery(this).parent().addClass('open');
				}
			});
		}
		jQuery('.full-sidenav .navbar-nav > li > a').next('.sub-menu,.mega-menu').slideUp();
		jQuery('.full-sidenav .sub-menu > li > a').next('.sub-menu').slideUp();

		jQuery('.full-sidenav .navbar-nav > li > a, .full-sidenav .sub-menu > li > a').unbind().on('click', function (e) {
			if (jQuery(this).hasClass('dz-open')) {

				jQuery(this).removeClass('dz-open');
				jQuery(this).parent('li').children('.sub-menu,.mega-menu').slideUp();
			} else {
				jQuery(this).addClass('dz-open');

				if (jQuery(this).parent('li').children('.sub-menu,.mega-menu').length > 0) {
					e.preventDefault();
					jQuery(this).next('.sub-menu,.mega-menu').slideDown();
					jQuery(this).parent('li').siblings('li').children('.sub-menu,.mega-menu').slideUp();
				} else {
					jQuery(this).next('.sub-menu,.mega-menu').slideUp();
				}
			}
		});
	}
	var handleSidebarMenu = function () {
		jQuery('.menu-btn').on('click', function () {
			jQuery(this).toggleClass('open');
			jQuery('.full-sidenav, .main-bar').toggleClass('show');
		});
		jQuery('.header.style-2 .menu-btn').on('click', function () {

			//for nav hidden
			if ($(this).hasClass('open')) {
				jQuery('.header.style-2 .header-nav').css({ "visibility": "visible", "opacity": "1" });
			} else {
				jQuery('.header.style-2 .header-nav').css({ "visibility": "hidden", "opacity": "0" });
			}
		});
		jQuery('.menu-close').on('click', function () {
			jQuery('.contact-sidebar').removeClass('active');
			jQuery('.menu-btn').removeClass('open');
		});
	}

	/* datetimepicker ============ */
	var datetimepicker = function (){
		if(($('#datetimepicker').length > 0)){
			$('#datetimepicker').datetimepicker();
		}
	}
		
	/* Split Box ============ */
	var isScrolledIntoView = function (elem){
		var $elem = $(elem);
		var $window = $(window);

		var docViewTop = $window.scrollTop();
		var docViewBottom = docViewTop + $window.height();

		var elemTop = $elem.offset().top;
		var elemBottom = elemTop + $elem.height();

		return ((elemBottom <= docViewBottom) && (elemTop >= docViewTop));
	}
	
	var splitImageAnimation = function (){
		$(window).on('scroll',function(){
			$('.split-box').each(function(){
				if(isScrolledIntoView($(this))){
					$(this).addClass('split-active');
				}
			});
		});
	}
	
	var handleDZhoverMove = function (){
		if(jQuery('.dz-hover-move').length > 0){
			const dzAnchorTags = document.querySelectorAll(".dz-hover-move li");
			dzAnchorTags.forEach((anchor) => {
				let sx = 0;
				let sy = 0;
				let ssize = 1;

				let dx = sx;
				let dy = sy;
				let dsize = ssize;

				let dzWidth = window.innerWidth;
				let dzHeight = window.innerHeight;

				anchor.addEventListener("mousemove", (e) => {
					const rect = anchor.getBoundingClientRect();
					const anchorCenterX = rect.left + rect.width / 2;
					const anchorCenterY = rect.top + rect.height / 2;

					dx = (e.clientX - anchorCenterX) * 0.90;
					dy = (e.clientY - anchorCenterY) * 0.90;

					dsize = 1.3;
				});

				anchor.addEventListener("mouseleave", (e) => {
					dx = 0;
					dy = 0;
					dsize = 1;
				});
			  
				window.addEventListener("resize", () => {
					dzWidth = window.innerWidth;
					dzHeight = window.innerHeight;
				});

				function lerp(a, b, t) {
					return (1 - t) * a + t * b;
				}

				function update() {
					sx = lerp(sx, dx, 0.1);
					sx = Math.floor(sx * 100) / 100;

					sy = lerp(sy, dy, 0.1);
					sy = Math.floor(sy * 100) / 100;

					ssize = lerp(ssize, dsize, 0.05);
					ssize = Math.floor(ssize * 100) / 100;

					anchor.style.transform = `translate(${sx}px, ${sy}px) scale(${ssize})`;
					requestAnimationFrame(update);
				}

				update();

			});
		}
	}
	var setCurrentYear = function(){
		const currentDate = new Date();
          let currentYear = currentDate.getFullYear();
		  
         let elements = document.getElementsByClassName('current-year'); 

		for (const element of elements) {
            element.innerHTML = currentYear;
        }

	}
	
	/* Scroll Top Progress ============ */
	var handleScrollTopProgress = function (){
		if(jQuery('.scroltop-progress').length > 0){
			var progressPath = $('.scroltop-progress path');
			var pathLength = progressPath[0].getTotalLength();
			var offset = 500;
			var duration = 550;

			progressPath.css({
				'transition': 'none',
				'WebkitTransition': 'none',
				'strokeDasharray': pathLength + ' ' + pathLength,
				'strokeDashoffset': pathLength
			});

			progressPath[0].getBoundingClientRect();

			progressPath.css({
				'transition': 'stroke-dashoffset 10ms linear',
				'WebkitTransition': 'stroke-dashoffset 10ms linear'
			});

			var updateProgress = function() {
			var scroll = $(window).scrollTop();
			var height = $(document).height() - $(window).height();
			var progress = pathLength - (scroll * pathLength / height);
				progressPath.css('strokeDashoffset', progress);
			};

			updateProgress();

			$(window).scroll(updateProgress);

			$(window).on('scroll', function() {
				if ($(this).scrollTop() > offset){
				  $('.scroltop-progress').addClass('active-progress');
				} else {
				  $('.scroltop-progress').removeClass('active-progress');
				}
			});

			$('.scroltop-progress').on('click', function(event) {
				event.preventDefault();
				$('html, body').animate({
					scrollTop: 0
				}, duration);
				return false;
			});
		}
	}
	
	var handleTilt = function(){
		if(jQuery('.dz-tilt').length > 0){			
			$('.dz-tilt').tilt({
				glare: true,
				maxGlare: .5
			})
		}
	}
	
	var handleBoxAware = function (){
		if(jQuery('.hover-aware').length > 0){	
			$('.hover-aware').on('mouseenter', function(e) {
				var parentOffset = $(this).offset(),
				relX = e.pageX - parentOffset.left,
				relY = e.pageY - parentOffset.top;
				$(this).find('.effect').css({top:relY, left:relX})
			})
			.on('mouseout', function(e) {
				var parentOffset = $(this).offset(),
				relX = e.pageX - parentOffset.left,
				relY = e.pageY - parentOffset.top;
				$(this).find('.effect').css({top:relY, left:relX})
			});
		}
	}

	/* Image workout-effect ============ */
	var handleImageworkout = function () {
		$('.image-workout-effect').hover(function () {
			var title = $(this).find('.title').text();
			var subTitle = $(this).find('.dz-name').text();
			$(this).data('tipText', title);
			$(this).closest('section').append("<div class='image-workout'>" + "<h4 class='title'>" + title + "</h4>" + "<h6 class='dz-name'>" + subTitle + "</h6>" + "</div>");
			var imageworkoutWidth = $(this).find('.dz-info').width() + 40;
			$('.image-workout').animate({ width: imageworkoutWidth }, 500, "linear");
		}, function () {
			// Hover out code
			$(this).find('span', $(this).data('tipText'));
			//$('.image-workout').remove();
		}).mousemove(function (e) {
			var mousex = e.pageX + 20; //Get X coordinates
			var mousey = e.pageY + 10; //Get Y coordinates
			$('.image-workout').css({ top: mousey, left: mousex })
			if (mousex + $('.image-workout').width() + 60 > screen.width) {
				$('.image-workout').css({ top: mousey, left: mousex - $('.image-workout').width() - 60 })
			}
		});
	}
	
	/* ParallaxScroll============ */
	var handleParallaxScroll = function(){
		if(jQuery('.dz-parallax').length > 0){
			$(window).on("load scroll", function() {
				var parallaxElements = $(".dz-parallax");

				window.requestAnimationFrame(function() {
					parallaxElements.each(function() {
						var currentElement = $(this),
							windowTop = $(window).scrollTop(),
							elementTop = currentElement.offset().top,
							elementHeight = currentElement.height(),
							viewPortHeight = window.innerHeight * 0.5 - elementHeight * 0.5,
							scrolled = windowTop - elementTop + viewPortHeight,
							customSpeed = currentElement.data("parallax-speed") || 0.1;

						currentElement.css({
							transform: "translate3d(0," + scrolled * -customSpeed + "px, 0) rotate(" + scrolled * -customSpeed + "deg)"
						});
					});
				});
			});	
		}
		if(jQuery('.bg-parallax').length > 0){
			$(window).on("scroll", function() {
				let offset = $(window).scrollTop();
				$(".bg-parallax").css("background-position-y", offset * 0.1 + "px");
			});	
		}
	}
	
	/* Handle Navbar Toggler ============ */
	var handleScreenLock = function(){
		jQuery('.navbar-toggler, .menu-btn').on('click',function(){
			jQuery('body').toggleClass('screen-lock');
			jQuery('.styleswitcher, .DZ-theme-btn').toggleClass('hide');
		});
	}
	
	/* Password Show / Hide */
	var handleShowPass = function(){
		jQuery('.show-pass').on('click',function(){
			var inputType = jQuery(this).parent().find('.dz-password');
			if(inputType.attr('type') == 'password')
			{
				inputType.attr('type', 'text');
				jQuery(this).addClass('active');
			}
			else
			{
				inputType.attr('type', 'password');
				jQuery(this).removeClass('active');
			}
		});
	}
	
	
	
	
	/* Function ============ */
	return {
		init:function(){
			setCurrentYear();
			smartWizard();
			datetimepicker();
			handleHomeSearch();
			handleBoxHover();
			handleDZhoverMove();
			handleImageworkout();
			handleDzTheme();
			handleBoxAware();
			handleTilt();
			handleMagnificPopup();
			splitImageAnimation();
			handleHeaderFix();
			handleSelectpicker();
			handleVideo();
			handlePlaceholderAnimation();
			handleScrollTopProgress();
			handleFilterMasonary();
			handleFinalCountDown();
			handleParallaxScroll();
			handelResize();
			jQuery('.modal').on('show.bs.modal', reposition);
			handleIconDropdowm();
			handleCurrentActive();
			handlePerfectScrollbar();
			handleShowPass();
			handleWowAnimation();
			handleBmiCalculator();
			priceslider();
			handleMagnifyGallery();
			handleLightgallery();
			handleSidebarMenu();
			handleScreenLock();
			handleBootstrapTouchSpin();
			dzTheme();
		},

		load:function(){
			handleCounter();
			handleMasonryBoxLoop();
			handleDzTheme();
		},
		
		resize:function(){
			screenWidth = $(window).width();
			handleFinalCountDown();
			handleDzTheme();
		}
	}
	
}();

/* Document.ready Start */	
jQuery(document).ready(function() {
    'use strict';
	
	PowerZone.init();
	
	$('a[data-bs-toggle="tab"]').click(function(){
		// todo remove snippet on bootstrap v5
		$('a[data-bs-toggle="tab"]').click(function() {
			$($(this).attr('href')).show().addClass('show active').siblings().hide();
		})
	});
	
	jQuery('.navicon').on('click',function(){
		$(this).toggleClass('open');
	});
	
	if($('[data-splitting]').length > 0){
		Splitting();
	}
	setInterval(function() {
		jQuery('[data-splitting]').toggleClass('active');
	}, 5000);
	
});
/* Document.ready END */



/* Window Load START */
 jQuery(window).on('load',function () {
	PowerZone.load();
	
	setTimeout(function(){		
		jQuery('#loading-area-2').addClass('active');
		jQuery('#loading-area-2').fadeOut(2500);
	}, 2500);
	setTimeout(function(){
		jQuery('#loading-area').fadeOut();
		jQuery('#loading-area-2').addClass('show');
	}, 2000);
}); 
/*  Window Load END */

/* Window Resize START */
jQuery(window).on('resize',function () {
	'use strict'; 
	PowerZone.resize();
});
/*  Window Resize END */

