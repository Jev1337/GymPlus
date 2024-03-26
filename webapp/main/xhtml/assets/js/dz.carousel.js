/**
	Template Name 	 : PowerZone
	Author			 : DexignZone
	Version			 : 1.0
	File Name	     : dz.carousel.js
	Author Portfolio : https://themeforest.net/user/dexignzone/portfolio
	
	Core script to handle the entire theme and core functions
	
**/

/* JavaScript Document */
jQuery(window).on('load', function() {
    'use strict';
	
	function changeItemBoxed() {
		if(jQuery("body").hasClass("boxed")) {
			return 3;
		} else {
			return 4;
		}
	}
	
	// Main Slider
	if(jQuery('.main-slider').length > 0){
		var swiperMain = new Swiper('.main-slider', {
			speed: 1000,
			slidesPerView: 1,
			loop:true,
			parallax: true,
			direction: 'vertical',
		 	//autoplay: {
			//  delay: 3000,
			//}, 
			pagination: {
	         	el: ".main-pagination",
	         	clickable: true,
	         	renderBullet: function (index, className) {
					return '<span class="' + className + '">' +"0"+ (index + 1) + "</span>";
				},
	        },
			navigation: {
	          	nextEl: ".main-btn-next",
				prevEl: ".main-btn-prev",
	        },
		});
	}
	
	// Main Slider-2
	if(jQuery('.main-slider-2').length > 0){
		var swiperMain = new Swiper('.main-slider-2', {
			speed: 1000,
			effect: "fade",
			slidesPerView: 1,
			loop:true,
		 	autoplay: {
			  delay: 2000,
			}, 
			pagination: {
	         	el: ".main-pagination",
	         	clickable: true,
	         	renderBullet: function (index, className) {
					return '<span class="' + className + '">' +"0"+ (index + 1) + "</span>";
				},
	        },
			navigation: {
	          	nextEl: ".main-btn-next",
				prevEl: ".main-btn-prev",
	        },
		});
	}
	
	// login-slider
	if(jQuery('.login-slider').length > 0){
		var swiper = new Swiper('.login-slider', {
			speed: 3000,
			effect: "fade",
			slidesPerView: 1,
			loop:true,
			autoplay: {
			  delay: 3000,
			},
			pagination: {
	         	el: ".main-pagination",
	         	clickable: true,
	         	renderBullet: function (index, className) {
					return '<span class="' + className + '">' +"0"+ (index + 1) + "</span>";
				},
	        },
			navigation: {
	          	nextEl: ".main-btn-next",
				prevEl: ".main-btn-prev",
	        },
		});
	}
		
	// trainer Slider
	if(jQuery('.trainer-slider').length > 0){
		var swiper = new Swiper('.trainer-slider', {
			speed: 1000,
			effect: "fade",
			slidesPerView: 1,
			loop:true,
			autoplay: {
			  delay: 3000,
			},
			navigation: {
				prevEl: ".main-btn-prev",
	          	nextEl: ".main-btn-next",
	        },
		});
	}	

	// Team Slider
	if(jQuery('.team-slider').length > 0){
		var swiper = new Swiper('.team-slider', {
			speed: 1500,
			slidesPerView: 3,
			spaceBetween: 30,
			loop:true,
			autoplay: {
			   delay: 3000,
			},
			pagination: {
	         	el: ".team-slider-pagination",
	         	clickable: true,
	         	renderBullet: function (index, className) {
					return '<span class="' + className + '">' +"0"+ (index + 1) + "</span>";
				},
	        },
			navigation: {
				nextEl: '.team-slider-next',
				prevEl: '.team-slider-prev',
			},
			breakpoints: {
				1200: {
					slidesPerView: 3,
				},
				992: {
					slidesPerView: 3,
				},
				500: {
					slidesPerView: 2,
					spaceBetween: 15,
				},
				320: {
					slidesPerView: 1,
					spaceBetween: 15,
				},
			}
		});
	}
	
	
	// Testimonial Swiper
	if(jQuery('.testimonial-swiper').length > 0){
		var swiper = new Swiper('.testimonial-swiper', {
			speed: 1500,
			parallax: true,
			slidesPerView: 1,
			spaceBetween: 0,
			loop:true,
			autoplay: {
			   delay: 3000,
			},
			navigation: {
	          	nextEl: ".testimonial-button-next",
	         	 prevEl: ".testimonial-button-prev",
	        },
			pagination: {
	         	 el: ".swiper-pagination",
	         	 clickable: true,
	         	 renderBullet: function (index, className) {
	           	 return '<span class="' + className + '">' +"0"+ (index + 1) + "</span>";
	          	},
	        },
		});
	}
	
	// Testimonial Swiper 1
	if(jQuery('.testimonial-swiper-1').length > 0){
		var swiper = new Swiper('.testimonial-swiper-1', {
			speed: 1500,
			parallax: true,
			slidesPerView:"auto",
			spaceBetween: 0,
			centeredSlides: true,
			loop:true,
			autoplay: {
			   delay: 3000,
			},
			pagination: {
	         	el: ".dz-swiper-pagination1",
	         	clickable: true,
	         	renderBullet: function (index, className) {
					return '<span class="' + className + '">' +"0"+ (index + 1) + "</span>";
				},
	        },
		});
	}
	
	// Testimonial Swiper 1
	if(jQuery('.testimonial-swiper-2').length > 0){
		var swiper = new Swiper('.testimonial-swiper-2', {
			speed: 1500,
			slidesPerView: 2,
			spaceBetween:20,
			loop:true,
			autoplay: {
			   delay: 3000,
			},
			breakpoints: {
				1280: {
					slidesPerView: 2,
				},
				991: {
					slidesPerView: 1,
				},
				767: {
					slidesPerView: 1,
				},
				320: {
					slidesPerView: 1,
				},
			}
			
			
		});
	}
	

	// Portfolio Slider
	if(jQuery('.portfolio-slider').length > 0){
		var swiper = new Swiper('.portfolio-slider', {
			speed: 1500,
			slidesPerView: "4",
			spaceBetween: 20,
			loop:true,
			autoplay: {
			   delay: 3000,
			},
			breakpoints: {
				1200: {
					slidesPerView: 4,
				},
				992: {
					slidesPerView: 4,
				},
				768: {
					slidesPerView: 3,
				},
				575: {
					slidesPerView: 2,
				},
				320: {
					slidesPerView: 1,
				},
			}
		});
	}
		
	
	// Portfolio Slider-1
	if(jQuery('.portfolio-slider-1').length > 0){
		var swiper = new Swiper('.portfolio-slider-1', {
			slidesPerView: 5,
			loop:true,
			speed: 1500,
			spaceBetween: 0,
			breakpoints: {
				1280: {
					slidesPerView: 5,	
				},
				768: {
					slidesPerView: 4,
				},
				767: {
					slidesPerView: 3,
					spaceBetween: 10,
				},
				320: {
					slidesPerView: 2,
				},
			}
		});
	}

	// Portfolio Slider-2
	if(jQuery('.portfolio-slider-2').length > 0){
		var swiper = new Swiper('.portfolio-slider-2', {
			speed: 1500,
			slidesPerView: 8,
			spaceBetween: 0,
			loop:true,
			autoplay: {
			   delay: 3000,
			},
			breakpoints: {
				1600: {
					slidesPerView: 8,
				},
				1480: {
					slidesPerView: 7,
				},
				768: {
					slidesPerView: 4,
				},
				320: {
					slidesPerView: 2,
				},
			}
		});
	}
	
	//  Product Gallery Swiper1
	if(jQuery('.product-gallery-swiper').length > 0){
		var swiper = new Swiper(".product-gallery-swiper", {
			spaceBetween: 15,
			slidesPerView: 4,
			freeMode: true,
			watchSlidesProgress: true,
		});
		var swiper2 = new Swiper(".product-gallery-swiper2", {
		  spaceBetween: 0,
		  updateOnWindowResize: true,	
		  thumbs: {
			swiper: swiper,
		  },
		});
	}
	
	
	// Post Swiper
	if(jQuery('.post-swiper').length > 0){
		var swiper = new Swiper('.post-swiper', {
			speed: 1500,
			parallax: true,
			slidesPerView: 1,
			spaceBetween: 0,
			loop:true,
			autoplay: {
			   delay: 3000,
			},
			navigation: {
				nextEl: '.next-post-swiper-btn',
				prevEl: '.prev-post-swiper-btn',
			}
		});
	}
	
	// Clients Swiper
	if(jQuery('.clients-swiper').length > 0){
		var swiper5 = new Swiper('.clients-swiper', {
			speed: 1500,
			parallax: true,
			slidesPerView: 5,
			spaceBetween: 30,
			autoplay: {
				delay: 3000,
			},
			loop:true,
			navigation: {
				nextEl: '.swiper-button-next5',
				prevEl: '.swiper-button-prev5',
			},
			breakpoints: {
				1200: {
					slidesPerView: 5,
				},
				992: {
					slidesPerView: 4,
				},
				768: {
					slidesPerView: 3,
				},
				575: {
					slidesPerView: 2,
				},
				320: {
					slidesPerView: 2,
				},
			}
		});
	}
	
});
/* JavaScript Document END */