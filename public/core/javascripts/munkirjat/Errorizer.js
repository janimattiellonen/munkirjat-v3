/**
 * @param jQuery object $containerElement
 * @param string elementClass
 * @constructor
 */
Munkirjat.Errorizer = function($containerElement, elementClass, aliases = null) {
    this.$containerElement  = $containerElement;
    this.elementClass       = elementClass;
    this.aliases			= aliases;
};

Munkirjat.Errorizer.prototype = (function()
{
    return {
        errorize: function(messages) {
            this.clear();
            var self = this;
            _.each(messages, function(values, key) {
            	console.log("ssss: " + JSON.stringify(values))
                self.errorizeElement(values);
            })
        },

        errorizeElement: function(error) {
        	
        	// error = "ssss: {"key":"title","message":"Title is required"}"
        	targetName = this.getMapping(error);
        	
            $(this.$containerElement).addClass('errorized');
            $errorDiv = $('<div class="col-sm-offset-3 col-sm-9"></div>').addClass('errorized-message');
            $ul = $('<ul></ul>');
            $ul.append($('<li></li>').text(i18n.t(error.message)));

            $errorDiv.html($ul);
            	
            $item = $('*[name="' + targetName + '"]', this.$containerElement).closest("." + this.elementClass);
            $item.append($errorDiv);
        },

        
        
        clear: function() {
            $(this.$containerElement).find('.errorized-message').remove();
        },
        
        getMapping: function(error) {
        	console.log("hah: " + JSON.stringify(error));
        	var value = _.filter(this.aliases, function(item) {
        		console.log("wur: " + JSON.stringify(item), "hah: " + JSON.stringify(error));
        		
        		return item.key === error.key;
        	}, error);
        	
        	console.log("value: " + JSON.stringify(value) + ", ee: " + error.key);
        	var retVal = value.length === 1 ? value[0].alias : error.key;
        	console.log("RETVAL: " + retVal);
        	return retVal;
        }
    }
})();