$(function() {
	queryNodeToJSON();           //find node and transfer them into json
});

var JsonData = {nodes:[]};
var links = [];
var simulation = null;

/**
 * 根据node节点名称查询数据,返回JSON
 * 
 * @returns
 */
function queryNodeToJSON(key) {
	if(key){
		$("#keyword").val(key);
	}
	var keyword = $("#keyword").val();
	if (keyword) {
		 $.get("/searchkey", {nodeName : keyword}, function(data) {  //!
			 JsonData.nodes = data;
			
			 links.length=0;
			// alert(JsonData.nodes[1].tname);
			 //links.map(JsonData=>{ return {source: JsonData.nodes[0].cname,target:JsonData.nodes[1].tname,type: "licensing"} });
			 for (var i=1;i<data.length;i++) 
				 {
				 var temlinks = [
					  {source: JsonData.nodes[i].tname, target:JsonData.nodes[0].cname , type: "licensing"},];
				 links.push.apply(links,temlinks);
						 
				 }
			
			 var nodes = {};

			// Compute the distinct nodes from the links.
			links.forEach(function(link) {
			  link.source = nodes[link.source] || (nodes[link.source] = {name: link.source});
			  link.target = nodes[link.target] || (nodes[link.target] = {name: link.target});
			});

			var width = 950,
			    height = 400;

			var force = d3.layout.force()
			    .nodes(d3.values(nodes))
			    .links(links)
			    .size([width, height])
			    .linkDistance(200)
			    .charge(-300)
			    .on("tick", tick)
			    .start();
			
			d3.select('svg').remove();   //删除整个SVG
			d3.select('svg')
			  .selectAll('*')
			  .remove();            

			var svg = d3.select("table").append("svg")
			    .attr("width", width)
			    .attr("height", height);

			// Per-type markers, as they don't inherit styles.
			svg.append("defs").selectAll("marker")
			    .data(["suit", "licensing", "resolved"])
			  .enter().append("marker")
			    .attr("id", function(d) { return d; })
			    .attr("viewBox", "-5 -5 10 10")
			    .attr("refX", 18)
			    .attr("refY", -1.5)
			    .attr("markerWidth", 8)
			    .attr("markerHeight", 8)
			    .attr("orient", "auto")
			  .append("path")
			    .attr("d", "M0,-5L10,0L0,5");

			var path = svg.append("g").selectAll("path")
			    .data(force.links())
			  .enter().append("path")
			    .attr("class", function(d) { return "link " + d.type; })
			    .attr("marker-end", function(d) { return "url(#" + d.type + ")"; });

			var circle = svg.append("g").selectAll("circle")
			    .data(force.nodes())
			  .enter().append("circle")
			    .attr("r", 15)			    
			    .call(force.drag)			    
			    .on("click" ,function(d){ 
			    	if(d.name==JsonData.nodes[0].cname) alert(JsonData.nodes[0].cintroduction);
			    	for (var i=1;i<JsonData.nodes.length;i++)
			    	{
			    		if(d.name==JsonData.nodes[i].tname) 
			    			alert(JsonData.nodes[i].tintroduction);
			    	} ; 
			    })
			    .style("fill",function(d) { if(d.name==JsonData.nodes[0].cname) return"LightPink"
			    else return "CornflowerBlue";});

			var text = svg.append("g").selectAll("text")
			    .data(force.nodes())
			  .enter().append("text")
			    .attr("x", 8)
			    .attr("y", ".31em")
			    .text(function(d) { return d.name; });

			// Use elliptical arc path segments to doubly-encode directionality.
			function tick() {
			  path.attr("d", linkArc);
			  circle.attr("transform", transform);
			  text.attr("transform", transform);
			}

			function linkArc(d) {
			  var dx = d.target.x - d.source.x,
			      dy = d.target.y - d.source.y,
			      dr = Math.sqrt(dx * dx + dy * dy);
			  return "M" + d.source.x + "," + d.source.y + "A" + dr + "," + dr + " 0 0,1 " + d.target.x + "," + d.target.y;
			}

			function transform(d) {
			  return "translate(" + d.x + "," + d.y + ")";
			}
		 });
	} else {
		layer.alert('Enter a keyword please!');
	}
	
	
	
}



