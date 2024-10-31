package org.ebics.client.ebicsrestapi

import org.ebics.client.ebicsrestapi.key.ApiKeyAuthenticationFilter
import org.ebics.client.ebicsrestapi.key.ApiKeyAuthenticationProvider
import org.ebics.client.ebicsrestapi.key.ApiKeyProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.ldap.authentication.AbstractLdapAuthenticationProvider
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationFilter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher


@Configuration
@EnableWebSecurity
class SecurityConfiguration {

    @Bean
    fun filterChainBasic(http: HttpSecurity, env: Environment): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize(HttpMethod.GET, "/auth", permitAll)
                authorize(HttpMethod.GET, "/bankconnections",hasAnyRole("ADMIN", "USER", "API", "GUEST"))
                authorize(AntPathRequestMatcher( "/bankconnections/{\\d+}/H00{\\d+}/**",HttpMethod.POST.name()),hasAnyRole("USER", "API", "GUEST"))
                authorize(AntPathRequestMatcher("/bankconnections/{\\d+}/H00{\\d+}/**", HttpMethod.GET.name()),hasAnyRole("USER", "API", "GUEST"))
                authorize(HttpMethod.POST, "/bankconnections",hasRole("USER"))
                authorize(AntPathRequestMatcher("/bankconnections/{\\d+}", HttpMethod.PUT.name()),hasRole("USER"))
                authorize(AntPathRequestMatcher("/bankconnections/{\\d+}",HttpMethod.DELETE.name()),hasAnyRole("ADMIN", "USER"))
                authorize(HttpMethod.GET, "/banks/**",hasAnyRole("ADMIN", "USER", "API", "GUEST"))
                authorize(HttpMethod.POST, "/banks/**",hasRole("ADMIN"))
                authorize(HttpMethod.PUT, "/banks/**",hasRole("ADMIN"))
                authorize(HttpMethod.PATCH, "/banks/**",hasRole("ADMIN"))
                authorize(HttpMethod.DELETE, "/banks/**",hasRole("ADMIN"))
                authorize(HttpMethod.GET, "/user",hasAnyRole("ADMIN", "USER", "API", "GUEST"))
                authorize(HttpMethod.GET, "/user/settings",hasAnyRole("ADMIN", "USER", "API", "GUEST"))
                authorize(HttpMethod.PUT, "/user/settings",hasAnyRole("ADMIN", "USER", "GUEST"))
                permitAll
            }

            cors { }
            csrf { disable() }
            formLogin { defaultSuccessUrl("/user", false) }
            logout { }

        }
        if (env.activeProfiles.contains("dev")) {
            http {
                formLogin { disable() }
                logout { disable() }
                httpBasic { }
            }
        }
        http.addFilterBefore(ApiKeyAuthenticationFilter(),UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}
