require 'json'
require 'jbuilder'
data=File.open("company.js",external_encoding:Encoding::UTF_8).readlines.join.gsub("var jsoncom =",'').gsub(/'/,'"').gsub(/;\s*$/,'')
raw=JSON.parse(data)

json=Jbuilder.encode do |json|
	raw["company"].each do |company|

		json.set!(company["code"]) do 
			fields=Hash[companyname: :name, shortname: :short_name, code: :code, id: :url]
			fields.each do |key,value|
				json.set! "#{value}",company["#{key}"]
			end
		end
	end
end

File.open("comapny.json","w") do |file|
	file.write json
end
