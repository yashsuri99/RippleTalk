import { RegisterFormFour } from '../features/register/components/RegisterFormFour/RegisterFormFour';
import { RegisterFormOne } from '../features/register/components/RegisterFormOne/RegisterFormOne';
import { RegisterFormThree } from '../features/register/components/RegisterFormThree/RegisterFormThree';
import { RegisterFormTwo } from '../features/register/components/RegisterFormTwo/RegisterFormTwo';
import data from '../data/codes.json';
import { RegisterFormFive } from '../features/register/components/RegisterFormFive/RegisterFormFive';
import { RegisterFormSix } from '../features/register/components/RegisterFormSix/RegisterFormSix';

export const determineModalContent = (step: number): JSX.Element => {
	switch (step) {
		case 1:
			return <RegisterFormOne />;

		case 2:
			return <RegisterFormTwo />;

		case 3:
			return <RegisterFormThree />;

		case 4:
			return <RegisterFormFour />;

		case 5:
			return <RegisterFormFive />;

		case 6:
			return <RegisterFormSix />;

		default:
			return <></>;
	}
};

export const countryCodeDropDown = (): JSX.Element[] => {
	let options = data
		.filter((country) => {
			if (country.code !== 'India') {
				return country;
			}
		})
		.map((country) => {
			return (
				<option
					value={`${country.dial_code} ${country.name}`}
					key={country.code}
				>
					{`${country.dial_code} ${country.name}`}
				</option>
			);
		});

	options.unshift(
		<option value={'+91 India'} key={'India'}>
			{'+91 India'}
		</option>
	);

	return options;
};
