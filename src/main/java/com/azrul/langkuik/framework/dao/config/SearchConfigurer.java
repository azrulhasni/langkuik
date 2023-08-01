/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.dao.config;

import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurationContext;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;

/**
 *
 * @author azrul
 */
public class SearchConfigurer  implements ElasticsearchAnalysisConfigurer {
    @Override
    public void configure(ElasticsearchAnalysisConfigurationContext context) {
        context.analyzer( "english" ).custom() 
                .tokenizer( "standard" ) 
                .tokenFilters( "lowercase", "snowball_english", "asciifolding" ); 

        context.tokenFilter( "snowball_english" ) 
                .type( "snowball" )
                .param( "language", "English" ); 
    }
}


